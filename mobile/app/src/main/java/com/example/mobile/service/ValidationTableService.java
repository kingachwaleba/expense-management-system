package com.example.mobile.service;

        import android.content.Context;
        import android.util.Log;
        import android.widget.Toast;
        import androidx.recyclerview.widget.RecyclerView;
        import com.example.mobile.config.ApiClient;
        import com.example.mobile.config.ApiInterface;
        import com.example.mobile.model.Wallet;
        import com.example.mobile.model.WalletModel;
        import org.jetbrains.annotations.NotNull;
        import java.util.List;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

public class ValidationTableService  {
    Context context;
    ApiInterface apiInterface;

    public ValidationTableService (Context context, RecyclerView wallet_rv) {
        this.context = context;
        this.apiInterface = new ApiClient().getService();
    }

    public ValidationTableService (Context context) {
        this.context = context;
        this.apiInterface = new ApiClient().getService();
    }

    public interface OnOneWalletCallback{
        void onOneWallet(WalletModel walletModel);
    }



}
