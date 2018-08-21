package academy.learnprogramming.top10downloader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FeedAdapter extends ArrayAdapter {
    private static final String TAG = "FeedAdapter";

    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<FeedEntry> applications;

    public FeedAdapter(@NonNull Context context, int resource, List<FeedEntry> applications) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.applications = applications;
    }

    @Override
    public int getCount() {
        return applications.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = layoutInflater.inflate(layoutResource, parent, false);

            viewHolder =  new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FeedEntry currentApp = applications.get(position);

        viewHolder.tvTitle.setText(currentApp.getTitle());
        viewHolder.tvPubDate.setText(currentApp.getPubDate());
        viewHolder.tvDescription.loadDataWithBaseURL(null, currentApp.getDescription(),"text/html", "utf-8", null);

        return convertView;
    }

    private class ViewHolder {
        final TextView tvTitle;
        final TextView tvPubDate;
        final WebView tvDescription;

        ViewHolder(View v){
            this.tvTitle = v.findViewById(R.id.tvTitle);
            this.tvPubDate = v.findViewById(R.id.tvPubDate);
            this.tvDescription = v.findViewById(R.id.tvDestription);
        }
    }
}
