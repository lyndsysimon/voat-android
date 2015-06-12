package co.voat.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.voat.android.api.SubscriptionsResponse;
import co.voat.android.api.VoatClient;
import co.voat.android.data.Subscription;
import co.voat.android.viewHolders.SubscriptionViewHolder;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by Jawn on 6/12/2015.
 */
public class SubscriptionsActivity extends BaseActivity {

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, SubscriptionsActivity.class);
        return intent;
    }

    @InjectView(R.id.list)
    RecyclerView subversesList;
    @InjectView(R.id.empty_root)
    View emptyView;

    private final Callback<SubscriptionsResponse> subscriptionsResponseCallback = new Callback<SubscriptionsResponse>() {
        @Override
        public void success(SubscriptionsResponse subscriptionsResponse, Response response) {
            if (subscriptionsResponse.success
                    && subscriptionsResponse.data != null
                    && !subscriptionsResponse.data.isEmpty()) {
                emptyView.setVisibility(View.GONE);
                subversesList.setAdapter(new SubscriptionAdapter(subscriptionsResponse.data));
            } else {
                emptyView.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void failure(RetrofitError error) {
            Timber.e(error.toString());
            Snackbar.make(getWindow().getDecorView(), getString(R.string.error), Snackbar.LENGTH_SHORT)
                    .show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptions);
        ButterKnife.inject(this);
        subversesList.setLayoutManager(new LinearLayoutManager(this));
        VoatClient.instance().getUserSubscriptions("Jawnnypoo", subscriptionsResponseCallback);
    }

    public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionViewHolder> {

        private List<Subscription> mValues;

        public Subscription getValueAt(int position) {
            return mValues.get(position);
        }

        public SubscriptionAdapter(List<Subscription> items) {
            mValues = items;
        }

        @Override
        public SubscriptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            SubscriptionViewHolder holder = SubscriptionViewHolder.create(parent);
            return holder;
        }

        @Override
        public void onBindViewHolder(final SubscriptionViewHolder holder, int position) {
            Subscription subscription = getValueAt(position);
            holder.bind(subscription);
            holder.itemView.setTag(R.id.list_position, position);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}
