package com.guanyin.sardar.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.guanyin.sardar.criminalintent.model.Crime;
import com.guanyin.sardar.criminalintent.model.CrimeLab;

import org.w3c.dom.Text;

import java.util.List;


// 维护一个recycle view 并处理和它相关的所有事务

// 步骤：
// 1.activity持有一个fragment对象，该对象对activity的视图中的framelayout进行填充
// 2.获取view对象，并获取该view对象中集成的recycler view控件
// 3.recycler view需要一个适配器，在这里也就是mvc架构，recycler view是v，adapter是c，list数据为m
// 4.控制器首先获取整个数据的数量 接下来根据数量决定将加载多少个条目显示出来，然后为将加载的条目创建缓存也就是viewholder对象
// 5.控制器从缓存中拿到条目的视图 并将m层的数据内容显示在视图上
// 6.至此完成整个加载过程
public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mCrimeAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        Log.e("CrimeListFragment", "创建视图");
        return view;
    }

    private void updateUI() {
        CrimeLab crimelab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimelab.getCrimes();

        mCrimeAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mCrimeAdapter);
    }

    // 匿名内部类 为recycler view创建view holder
    private class CrimeHolder extends RecyclerView.ViewHolder {
        public TextView mTitleTextView;

        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        public CrimeHolder(View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id
                    .list_item_crime_solved_check_box);


        }

        public void bindCrime(Crime crime) {
            mTitleTextView.setText(crime.getTitle());
            mDateTextView.setText(crime.getDate().toString());
            mSolvedCheckBox.setChecked(crime.isSolved());
        }
    }

    // 匿名内部类 为recycler view创建适配器
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }


}
