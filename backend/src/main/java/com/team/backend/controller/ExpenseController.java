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
        List<List<ExpenseDetail>> expenses = new ArrayList<>();

        System.out.println("0");
        for (Expense expense : expenseList) {
            List<ExpenseDetail> unpaidPayments = expense.getUnpaidPayments();
            for (ExpenseDetail e : unpaidPayments)
                System.out.println(unpaidPayments.size() + " " + e.getUser().getLogin() + " " + e.getExpense().getUser().getLogin() + " " + e.getCost());
            if (unpaidPayments.size() != 0) {
                expenses.add(unpaidPayments);
            }
        }

        List<DebtsHolder> debts = new ArrayList<>();
        List<Map<String, Object>> peopleTemp = walletService.findUserList(wallet);
        List<String> people = new ArrayList<>();

        for (Map<String, Object> map : peopleTemp)
            people.add(String.valueOf(map.get("login")));

        DebtsHolder debtsHolder = new DebtsHolder();

        for (List<ExpenseDetail> list : expenses) {
            for (ExpenseDetail ed : list) {
                debtsHolder = new DebtsHolder();

                debtsHolder.setDebtor(ed.getUser().getLogin());
                debtsHolder.setCreditor(String.valueOf(ed.getExpense().getUser().getLogin()));
                debtsHolder.setBalance(ed.getCost());

                debts.add(debtsHolder);
            }
        }

        System.out.println("I");
        for (DebtsHolder d : debts) {
            System.out.println(d.getDebtor() + " " + d.getCreditor() + " " + d.getBalance());
        }
        System.out.println();

        List<DebtsHolder> tempList = new ArrayList<>();
        for (DebtsHolder debt : debts) {
            tempList.add(debt.orderDebt());
        }

        System.out.println("I i pół");
        for (DebtsHolder d : tempList) {
            System.out.println(d.getDebtor() + " " + d.getCreditor() + " " + d.getBalance());
        }
        System.out.println();

        List<DebtsHolder> newList = new ArrayList<>(debts.stream()
                .collect(Collectors.toMap(
                        e -> new AbstractMap.SimpleEntry<>(e.getDebtor(), e.getCreditor()),
                        Function.identity(),
                        (a, b) -> new DebtsHolder(a.getDebtor(), a.getCreditor(), a.getBalance().add(b.getBalance()))
                        )
                )
                .values());

        System.out.println("II");
        for (DebtsHolder holder : newList) {
            System.out.println(holder.getDebtor() + " " + holder.getCreditor() + " " + holder.getBalance());
        }
        System.out.println();

        newList.removeIf(d -> d.getBalance().equals(BigDecimal.ZERO));

        System.out.println("III");
        for (DebtsHolder holder : newList) {
            System.out.println(holder.getDebtor() + " " + holder.getCreditor() + " " + holder.getBalance());
        }
        System.out.println();

        Comparator<DebtsHolder> compareByDebtor = Comparator.comparing(DebtsHolder::getDebtor);
        Comparator<DebtsHolder> compareByCreditor = Comparator.comparing(DebtsHolder::getCreditor);
        Comparator<DebtsHolder> comparator = compareByDebtor.thenComparing(compareByCreditor);

        newList = newList.stream().sorted(comparator).collect(Collectors.toList());

        System.out.println("IV");
        for (DebtsHolder holder : newList) {
            System.out.println(holder.getDebtor() + " " + holder.getCreditor() + " " + holder.getBalance());
        }
        System.out.println();

        SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> simpleDirectedWeightedGraph
                = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        for (String person : people) {
            simpleDirectedWeightedGraph.addVertex(person);
        }

        for (DebtsHolder d2 : newList) {
            if (d2.getBalance().doubleValue() > 0) {
                DefaultWeightedEdge e10 = simpleDirectedWeightedGraph.addEdge(d2.getDebtor(), d2.getCreditor());
                simpleDirectedWeightedGraph.setEdgeWeight(e10, d2.getBalance().doubleValue());
            }
            else {
                DefaultWeightedEdge e10 = simpleDirectedWeightedGraph.addEdge(d2.getCreditor(), d2.getDebtor());
                simpleDirectedWeightedGraph.setEdgeWeight(e10, (-1) * d2.getBalance().doubleValue());
            }
        }

        AsUndirectedGraph<String, DefaultWeightedEdge> undirectedGraph
                = new AsUndirectedGraph<>(simpleDirectedWeightedGraph);

        PatonCycleBase<String, DefaultWeightedEdge> patonCycleBase = new PatonCycleBase<>(undirectedGraph);

        List<GraphPath<String, DefaultWeightedEdge>> gp = new ArrayList<>(patonCycleBase.getCycleBasis().getCyclesAsGraphPaths());

        System.out.println("V");
        for (DefaultWeightedEdge edge : simpleDirectedWeightedGraph.edgeSet()) {
            System.out.println(simpleDirectedWeightedGraph.getEdgeSource(edge) + " --- "
                    + simpleDirectedWeightedGraph.getEdgeWeight(edge)
                    + " ---> " + simpleDirectedWeightedGraph.getEdgeTarget(edge));
        }

        System.out.println("GP");
        for (GraphPath<String, DefaultWeightedEdge> gp2 : gp) {
            System.out.println(gp2);
        }

        System.out.println("GET CYCLES");
        for (List<DefaultWeightedEdge> temp : patonCycleBase.getCycleBasis().getCycles()) {
            System.out.println(temp);
        }

        while (gp.size() != 0) {
            GraphPath<String, DefaultWeightedEdge> element = gp.get(0);

            List<DefaultWeightedEdge> edgeList = element.getEdgeList();
            System.out.println("element.getVertexList()");

            for (String v : element.getVertexList()) {
                System.out.println(v);
            }

            System.out.println("EDGE LIST");
            for (DefaultWeightedEdge defaultWeightedEdge : edgeList) {
                System.out.println(defaultWeightedEdge + " " + simpleDirectedWeightedGraph.getEdgeWeight(defaultWeightedEdge));
            }

            Set<String> cycleVertices2 = new HashSet<>(element.getVertexList());
            List<String> vertexes = new ArrayList<>();

            for (int j = 0; j < cycleVertices2.size(); j++) {
                vertexes.add(element.getVertexList().get(j) + element.getVertexList().get(j + 1));
            }

//            for (DefaultWeightedEdge defaultWeightedEdge : edgeList) {
//                System.out.println(defaultWeightedEdge + " " + simpleDirectedWeightedGraph.getEdgeWeight(defaultWeightedEdge));
//                vertexes.add(simpleDirectedWeightedGraph.getEdgeSource(defaultWeightedEdge)
//                        + simpleDirectedWeightedGraph.getEdgeTarget(defaultWeightedEdge));
//            }

            System.out.println("VERTEXES ");
            for (String s : vertexes) {
                System.out.println(s);
            }

            AsSubgraph<String, DefaultWeightedEdge> tempGraph
                    = new AsSubgraph<>(simpleDirectedWeightedGraph, cycleVertices2);

            Map<String, Double> weightMap = new HashMap<>();
            for (DefaultWeightedEdge edge : tempGraph.edgeSet()) {
                weightMap.put(tempGraph.getEdgeSource(edge) + tempGraph.getEdgeTarget(edge), tempGraph.getEdgeWeight(edge));
            }

            System.out.println("Weight map");
            for (String wM : weightMap.keySet()) {
                System.out.println(wM + " " + weightMap.get(wM));
            }

            double min = Collections.min(weightMap.values());
            String minEdge = "";

            for (Map.Entry<String, Double> ar : weightMap.entrySet()){
                System.out.println("###########################");
                if (ar.getValue() == min) {
                    minEdge = ar.getKey();
                    break;
                }
            }

            System.out.println("!!!!!!!");
            System.out.println(minEdge + " " + min);

            List<String> vertexList = element.getVertexList();
//            vertexList.remove(vertexList.get(vertexList.size() - 1));
            System.out.println("Old vertex list");
            for (String ve : vertexList) {
                System.out.println(ve);
            }

            List<String> edges = new ArrayList<>();
            for (String ver : vertexList) {
                edges.add(vertexList.get(vertexList.indexOf(ver)) + vertexList.get(vertexList.indexOf(ver) + 1));
            }

            System.out.println("EDGES");
            for (String e : edges) {
                System.out.println(e);
            }

            Map<Integer, String> newVertexMap = new HashMap<>();

            int k = 0;
            if (edges.contains(minEdge)) {

                for (int l = vertexList.size() - 1; l >= 1; l--) {
                    newVertexMap.put(k, vertexList.get(l));
                    k++;
                }
                k--;

                System.out.println("New vertex list");
                for (String ve : newVertexMap.values()) {
                    System.out.println(ve);
                }
            }
            else {
                for (int l = 0; l < vertexList.size() - 1; l++) {
                    newVertexMap.put(k, vertexList.get(l));
                    k++;
                }
                k--;

                System.out.println("New vertex list");
                for (String ve : newVertexMap.values()) {
                    System.out.println(ve);
                }
            }

//            List<DefaultWeightedEdge> tempEdgeList = new ArrayList<>(tempGraph.edgeSet());
//            while (tempEdgeList.size() != 0) {
//
//            }

            for (DefaultWeightedEdge ed : tempGraph.edgeSet()) {
                double newWeight = 0.0;

                System.out.println("EDGE = " + ed);

                System.out.println("Vertexes contains");
                System.out.println(tempGraph.getEdgeSource(ed) + tempGraph.getEdgeTarget(ed));

//                if (vertexes.contains(tempGraph.getEdgeSource(ed) + tempGraph.getEdgeTarget(ed))) {
//                    System.out.println("IF TAK");
//                    newWeight = tempGraph.getEdgeWeight(ed) - min;
//                }
//                else {
//                    System.out.println("IF NIE");
//                    newWeight = tempGraph.getEdgeWeight(ed) + min;
//                }

                String source = tempGraph.getEdgeSource(ed);
                String target = tempGraph.getEdgeTarget(ed);

                Integer sourceKey = Integer.valueOf("1");
                Integer targetKey = Integer.valueOf("1");
//                for (Integer key : newVertexMap.keySet())
//                {
//                    if (newVertexMap.get(key).equals(source) )
//                    {
//                        sourceKey = key;
//                    }
//                    if (newVertexMap.get(key).equals(target) )
//                    {
//                        targetKey = key;
//                    }
//                }

                for (Map.Entry<Integer, String> arr : newVertexMap.entrySet()){
                    System.out.println("###########################");
                    System.out.println(arr.getValue().equals(source));
                    System.out.println(arr.getValue().equals(target));
                    System.out.println(arr.getValue());
                    System.out.println(arr.getKey());
                    if (arr.getValue().equals(source))
                        sourceKey = arr.getKey();
                    if (arr.getValue().equals(target))
                        targetKey = arr.getKey();
                }

                System.out.println("Source " + source + " key " + sourceKey);
                System.out.println("Target " + target + " key " + targetKey);

                System.out.println("K = " + k);
                if (sourceKey > targetKey) {
                    System.out.println("IF TAK");
                    newWeight = tempGraph.getEdgeWeight(ed) - min;
                }
                else if (sourceKey == 0 && targetKey == k) {
                    System.out.println("IF NIE");
                    newWeight = tempGraph.getEdgeWeight(ed) - min;
                }
                else if (sourceKey < targetKey) {
                    System.out.println("IF 0");
                    newWeight = tempGraph.getEdgeWeight(ed) + min;
                }

                if (newWeight == 0) {
                    simpleDirectedWeightedGraph.removeEdge(ed);
                }
                else
                    simpleDirectedWeightedGraph.setEdgeWeight(ed, newWeight);

                System.out.println("New edge weight = " + newWeight);
            }

            undirectedGraph = new AsUndirectedGraph<>(simpleDirectedWeightedGraph);
            patonCycleBase = new PatonCycleBase<>(undirectedGraph);
            gp = new ArrayList<>(patonCycleBase.getCycleBasis().getCyclesAsGraphPaths());
        }
        System.out.println();

        System.out.println("VI");
        for (DefaultWeightedEdge edge : simpleDirectedWeightedGraph.edgeSet()) {
            System.out.println(simpleDirectedWeightedGraph.getEdgeSource(edge) + " --- "
                    + simpleDirectedWeightedGraph.getEdgeWeight(edge)
                    + " ---> " + simpleDirectedWeightedGraph.getEdgeTarget(edge));
        }

        return new ResponseEntity<>("Aaaa", HttpStatus.OK);
    }
}
