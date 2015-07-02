package swipe.android.nearlings.core;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.readystatesoftware.viewbadger.BadgeView;
import com.throrinstudio.android.library.widgets.dashboard.DashBoardAdapter;
import com.throrinstudio.android.library.widgets.dashboard.DashBoardElement;

import java.util.ArrayList;

import swipe.android.DatabaseHelpers.MessagesDatabaseHelper;
import swipe.android.DatabaseHelpers.NeedsDetailsDatabaseHelper;
import swipe.android.nearlings.NearlingsContentProvider;

/**
 * Created by Cygnus on 6/26/15.
 */
public class CustomDashboardAdapter  extends DashBoardAdapter{

        public CustomDashboardAdapter(Context c){
            super(c);

        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            final ViewHandler holder;

            if(convertView == null) {
                vi 				= mInflater.inflate(com.throrinstudio.android.library.widgets.dashboard.R.layout.widget_dashboard_item, null);
                holder			= new ViewHandler();
                holder.image	= (ImageView) vi.findViewById(com.throrinstudio.android.library.widgets.dashboard.R.id.dashboard_item_img);
                holder.text 	= (TextView) vi.findViewById(com.throrinstudio.android.library.widgets.dashboard.R.id.dashboard_item_name);
                vi.setTag(holder);
            } else {
                holder = (ViewHandler) vi.getTag();
            }

            DashBoardElement element = getItem(position);

            if(element.getRes() instanceof Integer){
                holder.image.setImageResource((Integer) element.getRes());
            }else if(element.getRes() instanceof Drawable){
                holder.image.setImageDrawable((Drawable) element.getRes());
            }else if(element.getRes() instanceof Bitmap){
                holder.image.setImageBitmap((Bitmap) element.getRes());
            }

            holder.text.setText(element.getLibelle());

            if(position == 0) {
                BadgeView badge = new BadgeView(mContext, holder.image);
                String selectionClause = MessagesDatabaseHelper.COLUMN_UNREAD + " = ?";
                String[] mSelectionArgs = { "" };
                mSelectionArgs[0] = "True";
                CursorLoader cursorLoader = new CursorLoader(
                        mContext,
                        NearlingsContentProvider
                                .contentURIbyTableName(MessagesDatabaseHelper.TABLE_NAME),
                        MessagesDatabaseHelper.COLUMNS,  selectionClause,
                        mSelectionArgs,
                        MessagesDatabaseHelper.COLUMN_DATE + " DESC");
                Cursor c = cursorLoader.loadInBackground();
                if (c.getCount() != 0) {
                    badge.setText(String.valueOf(c.getCount()));
                    badge.setTextColor(Color.WHITE);
                    badge.show();
                }
            }
            return vi;
        }


        public static class ViewHandler{
            public ImageView 	image;
            public TextView 	text;
        }
}
