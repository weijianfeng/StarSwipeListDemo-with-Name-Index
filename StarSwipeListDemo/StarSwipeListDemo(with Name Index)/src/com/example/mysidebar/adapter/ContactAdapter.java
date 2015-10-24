package com.example.mysidebar.adapter;

import java.util.Collections;
import java.util.List;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.litepal.crud.DataSupport;

import com.example.mysidebar.R;
import com.example.mysidebar.model.Contact;
import com.example.mysidebar.utils.PinyinComparator;
import com.example.mysidebar.utils.PinyinUtils;
import com.fortysevendeg.swipelistview.SwipeListView;

import android.content.Context;
import android.net.NetworkInfo.State;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter implements SectionIndexer {

    /**
     * 联系人列表
     */
    private List<Contact> contacts = null;

    /**
     * 上下文对象
     */
    private Context context = null;

    private SwipeListView mSwipeListView;
    /**
     * 布局解析器
     */
    private LayoutInflater layoutInflater = null;

    /**
     * 构造方法
     * 
     * @param contacts
     *            联系人列表
     * @param context
     *            上下文对象
     */
    public ContactAdapter(Context context, List<Contact> contacts,
            SwipeListView swipeListView) {
        this.contacts = contacts;
        this.context = context;
        this.mSwipeListView = swipeListView;

        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        return contacts == null ? 0 : contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts == null ? null : contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void refresh(List<Contact> newcontacts) {
        contacts = newcontacts;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final Contact contact = (Contact) getItem(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = layoutInflater.inflate(R.layout.contact_item, null);

            viewHolder.tvCatalog = (TextView) convertView
                    .findViewById(R.id.tv_catalog);
            viewHolder.tvContactName = (TextView) convertView
                    .findViewById(R.id.tv_contact_name);
            viewHolder.btStar = (ImageButton) convertView
                    .findViewById(R.id.bt_star);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (contact.isStar()) {
            viewHolder.btStar.setImageDrawable(context
                    .getDrawable(R.drawable.star));
        } else {
            viewHolder.btStar.setImageDrawable(context
                    .getDrawable(R.drawable.no_star));
        }

        viewHolder.btStar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!contact.isStar()) {
                    contact.setFirstLetter("☆");
                    contact.setStar(true);
                } else {
                    String firstLetter = null;
                    try {
                        firstLetter = PinyinUtils
                                .getPinyinOfHanyu(contact.getName())
                                .substring(0, 1).toUpperCase();
                    } catch (BadHanyuPinyinOutputFormatCombination e) {
                        e.printStackTrace();
                    }
                    if (firstLetter.matches("[A-Z]")) {
                        contact.setFirstLetter(firstLetter);
                    } else {
                        contact.setFirstLetter("#");
                    }
                    contact.setFirstLetter(firstLetter);
                    contact.setStar(false);
                }
                contact.save();
                mSwipeListView.closeAnimate(position);
                Collections.sort(contacts, new PinyinComparator());
                refresh(contacts);
            }
        });

        /*
         * 判断是显示catalog还是联系人姓名
         * 
         * 如果当前position等于首字母第一次出现在ListView中的位置，那么就人为是catalog，否则就是联系人姓名
         */
        int section = getSectionForPosition(position);
        if (position == getPositionForSection(section)) {
            viewHolder.tvCatalog.setVisibility(View.VISIBLE);
            viewHolder.tvCatalog.setText(contact.getFirstLetter());
        } else {
            viewHolder.tvCatalog.setVisibility(View.GONE);
        }

        viewHolder.tvContactName.setText(contact.getName());

        return convertView;
    }

    private static class ViewHolder {
        TextView tvCatalog = null;
        TextView tvContactName = null;
        ImageButton btStar = null;
    }

    /**
     * 根据position，得到当前位置的item（即一个联系人）的首字母的ASCII值
     */
    @Override
    public int getSectionForPosition(int position) {

        if (contacts != null) {
            return contacts.get(position).getFirstLetter().charAt(0);
        }

        return -1;
    }

    /**
     * 根据首字母的ASCII的值，获取该字母在ListView第一次出现的位置
     */
    @Override
    public int getPositionForSection(int section) {

        for (int i = 0; i < getCount(); i++) {
            char firstChar = contacts.get(i).getFirstLetter().charAt(0);

            if (section == firstChar) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

}
