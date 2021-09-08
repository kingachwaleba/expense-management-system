package com.team.backend.controller;

import com.team.backend.helpers.DebtsHolder;
import com.team.backend.helpers.ExpenseHolder;
import com.team.backend.model.Expense;
import com.team.backend.model.ExpenseDetail;
import com.team.backend.model.Wallet;
import com.team.backend.service.ExpenseService;
import com.team.backend.service.WalletService;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.cycle.PatonCycleBase;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.AsUndirectedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
public class ExpenseController {

    private final WalletService walletService;
    private final ExpenseService expenseService;

    public ExpenseController(WalletService walletService, ExpenseService expenseService) {
        this.walletService = walletService;
        this.expenseService = expenseService;
    }

    @GetMapping("/expense/{id}")
    public ResponseEntity<?> one(@PathVariable int id) {
        Expense expense = expenseService.findById(id).orElseThrow(RuntimeException::new);

        return new ResponseEntity<>(expense, HttpStatus.OK);
    }

    @GetMapping("/wallet/{id}/expenses")
    public ResponseEntity<?> all(@PathVariable int id) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);

        return new ResponseEntity<>(expenseService.findAllByWalletOrderByDate(wallet), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/wallet/{id}/add-expense")
    public ResponseEntity<?> addExpense(@PathVariable int id, @Valid @RequestBody ExpenseHolder expenseHolder) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);

        expenseService.save(expenseHolder, wallet);

        return new ResponseEntity<>(expenseHolder.getExpense(), HttpStatus.OK);
    }

    @PutMapping("/expense/{id}")
    public ResponseEntity<?> editOne(@PathVariable int id, @RequestBody Expense newExpense) {
        Expense updatedExpense = expenseService.findById(id).orElseThrow(RuntimeException::new);

        updatedExpense.setName(newExpense.getName());
        updatedExpense.setTotal_cost(newExpense.getTotal_cost());

        for (ExpenseDetail expenseDetail : updatedExpense.getExpenseDetailSet()) {
            BigDecimal cost = updatedExpense.getTotal_cost().divide(BigDecimal.valueOf(updatedExpense
                    .getExpenseDetailSet().size()), 2, RoundingMode.CEILING);

            expenseDetail.setCost(cost);
        }

        updatedExpense.setCategory(newExpense.getCategory());
        updatedExpense.setPeriod(newExpense.getPeriod());

        expenseService.save(updatedExpense);

        return new ResponseEntity<>(updatedExpense, HttpStatus.OK);
    }

    @DeleteMapping("/expense/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        Expense expense = expenseService.findById(id).orElseThrow(RuntimeException::new);

        expenseService.delete(expense);

        return new ResponseEntity<>("The given expense was deleted!", HttpStatus.OK);
    }

    @GetMapping("/wallet/{id}/balance")
    public ResponseEntity<?> getWalletBalance(@PathVariable int id) {
        Wallet wallet = walletService.findById(id).orElseThrow(RuntimeException::new);

        List<Expense> expenseList = expenseService.findAllByWalletOrderByDate(wallet);
        List<DebtsHolder> debts = new ArrayList<>();

        // Find unpaid payments for all expenses in the given wallet
        for (Expense expense : expenseList) {
            List<ExpenseDetail> unpaidPayments = expense.getUnpaidPayments();
            if (unpaidPayments.size() != 0) {
                for (ExpenseDetail expenseDetail : unpaidPayments) {
                    DebtsHolder debtsHolder = new DebtsHolder();

                    debtsHolder.setDebtor(expenseDetail.getUser().getLogin());
                    debtsHolder.setCreditor(String.valueOf(expenseDetail.getExpense().getUser().getLogin()));
                    debtsHolder.setBalance(expenseDetail.getCost());
                    debtsHolder.setId(expenseDetail.getId());

                    debts.add(debtsHolder);
                }
            }
        }

        // Find people belong to the wallet
        List<String> people = wallet.getUserList();

        // Order debts that the debtor comes before the creditor
        debts.forEach(DebtsHolder::orderDebt);

        // Group together all debts owed by the same debtor to the same creditor and count their final balance
        debts = new ArrayList<>(debts.stream()
                .collect(Collectors.toMap(
                        e -> new AbstractMap.SimpleEntry<>(e.getDebtor(), e.getCreditor()),
                        Function.identity(),
                        (a, b) -> new DebtsHolder(a.getDebtor(), a.getCreditor(), a.getBalance().add(b.getBalance()), a.getId())
                        )
                )
                .values());

        // Remove an element if the balance between two users is equal to 0
        debts.removeIf(d -> d.getBalance().equals(BigDecimal.ZERO));

        // Check if the balance is equal to 0 and change the debtor with the creditor and balance to positive
        debts.stream()
                .filter(debtsHolder -> debtsHolder.getBalance().compareTo(BigDecimal.ZERO) < 0)
                .forEach(DebtsHolder::changeBalance);

        SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> simpleDirectedWeightedGraph
                = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        people.forEach(simpleDirectedWeightedGraph::addVertex);
        debts.forEach(debtsHolder -> simpleDirectedWeightedGraph.setEdgeWeight(simpleDirectedWeightedGraph
                    .addEdge(debtsHolder.getDebtor(), debtsHolder.getCreditor()),
                    debtsHolder.getBalance().doubleValue())
        );

        AsUndirectedGraph<String, DefaultWeightedEdge> undirectedGraph
                = new AsUndirectedGraph<>(simpleDirectedWeightedGraph);

        PatonCycleBase<String, DefaultWeightedEdge> patonCycleBase = new PatonCycleBase<>(undirectedGraph);
        List<GraphPath<String, DefaultWeightedEdge>> graphPathList = new ArrayList<>(patonCycleBase.getCycleBasis().getCyclesAsGraphPaths());

        while (graphPathList.size() != 0) {
            GraphPath<String, DefaultWeightedEdge> element = graphPathList.get(0);

            AsSubgraph<String, DefaultWeightedEdge> cycleSubgraph
                    = new AsSubgraph<>(simpleDirectedWeightedGraph, new HashSet<>(element.getVertexList()));

            Map<String, Double> weightMap = new HashMap<>();
            for (DefaultWeightedEdge defaultWeightedEdge : cycleSubgraph.edgeSet()) {
                weightMap.put(cycleSubgraph.getEdgeSource(defaultWeightedEdge)
                        + cycleSubgraph.getEdgeTarget(defaultWeightedEdge),
                        cycleSubgraph.getEdgeWeight(defaultWeightedEdge));
            }

            double minValue = Collections.min(weightMap.values());
            String minEdge = Objects
                    .requireNonNull(weightMap
                    .entrySet()
                    .stream()
                    .min(Map.Entry.comparingByValue())
                    .orElse(null)
            )
                    .getKey();

            List<String> vertexList = element.getVertexList();

            List<String> edgeList = new ArrayList<>();
            for (String ver : vertexList)
                edgeList.add(vertexList.get(vertexList.indexOf(ver)) + vertexList.get(vertexList.indexOf(ver) + 1));

            List<String> newVertexList = new ArrayList<>();

            if (edgeList.contains(minEdge))
                for (int l = vertexList.size() - 1; l >= 1; l--)
                    newVertexList.add(vertexList.get(l));
            else
                for (int l = 0; l < vertexList.size() - 1; l++)
                    newVertexList.add(vertexList.get(l));


            for (DefaultWeightedEdge ed : cycleSubgraph.edgeSet()) {
                double newWeight = 0.0;

                String source = cycleSubgraph.getEdgeSource(ed);
                String target = cycleSubgraph.getEdgeTarget(ed);

                int sourceKey = 0;
                int targetKey = 0;

                for (String string : newVertexList) {
                    if (string.equals(source))
                        sourceKey = newVertexList.indexOf(string);
                    if (string.equals(target))
                        targetKey = newVertexList.indexOf(string);
                }

                if (sourceKey > targetKey)
                    newWeight = cycleSubgraph.getEdgeWeight(ed) - minValue;
                else if (sourceKey == 0 && targetKey == newVertexList.size() - 1)
                    newWeight = cycleSubgraph.getEdgeWeight(ed) - minValue;
                else if (sourceKey < targetKey)
                    newWeight = cycleSubgraph.getEdgeWeight(ed) + minValue;

                if (newWeight == 0)
                    simpleDirectedWeightedGraph.removeEdge(ed);
                else
                    simpleDirectedWeightedGraph.setEdgeWeight(ed, newWeight);
            }

            undirectedGraph = new AsUndirectedGraph<>(simpleDirectedWeightedGraph);
            patonCycleBase = new PatonCycleBase<>(undirectedGraph);
            graphPathList = new ArrayList<>(patonCycleBase.getCycleBasis().getCyclesAsGraphPaths());
        }

        for (DefaultWeightedEdge edge : simpleDirectedWeightedGraph.edgeSet()) {
            System.out.println(simpleDirectedWeightedGraph.getEdgeSource(edge) + " --- "
                    + simpleDirectedWeightedGraph.getEdgeWeight(edge)
                    + " ---> " + simpleDirectedWeightedGraph.getEdgeTarget(edge));
        }

        return new ResponseEntity<>("Aaaa", HttpStatus.OK);
    }
}
