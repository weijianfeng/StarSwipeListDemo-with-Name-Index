package com.example.mysidebar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.litepal.crud.DataSupport;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mysidebar.adapter.ContactAdapter;
import com.example.mysidebar.model.Contact;
import com.example.mysidebar.utils.PinyinComparator;
import com.example.mysidebar.utils.PinyinUtils;
import com.example.mysidebar.widget.SideBar;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;

/**
 * 主界面
 * 
 * @author owen
 */
public class MainActivity extends Activity implements
        SideBar.onLetterTouchedChangeListener, SideBar.BackToListTopListener {

    private TextView       textViewDialog = null;
    private SideBar        sideBar        = null;

    private SwipeListView  mSwipeListView;

    private ContactAdapter contactAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mSwipeListView = (SwipeListView) findViewById(R.id.example_lv_list);
        textViewDialog = (TextView) findViewById(R.id.textViewDialog);
        sideBar = (SideBar) findViewById(R.id.siderbar);
        sideBar.setTextViewDialog(textViewDialog);
        sideBar.setOnLetterTouchedChangeListener(this);
        sideBar.setBackToListTopListener(this);

        contactAdapter = new ContactAdapter(MainActivity.this,
                generateContacts(), mSwipeListView);
        mSwipeListView.setAdapter(contactAdapter);
        mSwipeListView
                .setSwipeListViewListener(new BaseSwipeListViewListener());
        reload();
    }

    /**
     * 生成联系人数据
     */
    private List<Contact> generateContacts() {
        String[] contactArray = getResources().getStringArray(R.array.contacts);
        List<Contact> contacts = new ArrayList<Contact>(contactArray.length);

        if (DataSupport.count("Contact") > 0) {
            contacts = DataSupport.findAll(Contact.class);
            Collections.sort(contacts, new PinyinComparator());
        } else {
            for (int i = 0; i < contactArray.length; i++) {
                try {
                    Contact contact = new Contact();

                    contact.setName(contactArray[i]);
                    contact.setStar(false);

                    String firstLetter = PinyinUtils
                            .getPinyinOfHanyu(contactArray[i]).substring(0, 1)
                            .toUpperCase();
                    if (firstLetter.matches("[A-Z]")) {
                        contact.setFirstLetter(firstLetter);
                    } else {
                        contact.setFirstLetter("#");
                    }

                    contacts.add(contact);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            }

            // 对联系人列表按照 ABCDEFG...# 的顺序进行排序
            Collections.sort(contacts, new PinyinComparator());
            DataSupport.saveAll(contacts);
        }

        // contacts.add(0, new Contact("@@","☆"));

        return contacts;
    }

    @Override
    public void onTouchedLetterChange(String letterTouched) {
        // 联系人列表随着导航栏的滑动而滑动到相应的位置
        int position = contactAdapter.getPositionForSection(letterTouched
                .charAt(0));
        if (position != -1) {
            mSwipeListView.setSelection(position);
        }
    }

    private int getDeviceWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    private void reload() {
        mSwipeListView.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
        mSwipeListView.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL);
        // mSwipeListView.setSwipeActionRight(settings.getSwipeActionRight());
        mSwipeListView.setOffsetLeft(getDeviceWidth() * 2 / 3);
        // mSwipeListView.setOffsetRight(convertDpToPixel(settings.getSwipeOffsetRight()));
        mSwipeListView.setAnimationTime(0);
        mSwipeListView.setSwipeOpenOnLongPress(false);
    }

    @Override
    public void backtoTop(boolean isbacktoTop) {
        // TODO Auto-generated method stub
        if (isbacktoTop) {
            mSwipeListView.smoothScrollToPosition(0);
        }
    }

}
