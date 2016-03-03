package com.exe.googleplay.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.exe.googleplay.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by user on 2016/3/1.
 */
public class FlowLayout extends ViewGroup {

    private int DEFAULT_SPACING = 10;//默认的间距是10
    private int horizontalSpacing = DEFAULT_SPACING;//子view水平方向的间距
    private int verticalSpacing = DEFAULT_SPACING;//每行之间的垂直间距

    private ArrayList<Line> lineList = new ArrayList<Line>();//用来保存每一行line
    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 分行和测量FlowLayout的宽高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        lineList.clear();
        int width = MeasureSpec.getSize(widthMeasureSpec);//获得控件的宽度,是包含paddingLeft和paddingRight
        //这个宽才是我们需要比较用的宽
        int noPaddingWidth = width-getPaddingLeft()-getPaddingRight();//获取除去padding后的width
        int height = MeasureSpec.getSize(heightMeasureSpec);//获取控件的高度

        Line line = null;//声明为局部变量
        for(int i=0; i<getChildCount(); i++){
            View childView = getChildAt(i);//获得当前子view
            //1.先测量子view，是为了保证一定能够获取到宽高
            childView.measure(0, 0);//系统会按照自己的规则去测量

            if(line==null){
                line = new Line();
            }

            //2.如果当前line还没有子view，则直接放入，不用判断,因为要保证每行至少有一个view
            if(line.getViewList().size()==0){
                line.addView(childView);
            }else {
                //当前line中有子view
                if(line.getWidth()+horizontalSpacing+childView.getMeasuredWidth()>noPaddingWidth){
                    //表示需要换行,先存放之前的line，然后在重新new Line
                    lineList.add(line);

                    line = new Line();
                    line.addView(childView);
                    //如果当前childView是最后一个子view，则需要保存line
                    if(i==(getChildCount()-1)){
                        lineList.add(line);
                    }
                }else {
                    //不需要换行,直接添加到当前line
                    line.addView(childView);
                    //如果当前childView是最后一个子view，则需要保存line
                    if(i==(getChildCount()-1)){
                        lineList.add(line);
                    }
                }
            }
        }
        line = null;

        //计算当前FlowLayout的总高度
        for (int i = 0; i < lineList.size(); i++) {
            height += lineList.get(i).getHeight();
        }
        height += verticalSpacing*(lineList.size()-1)+getPaddingTop()+getPaddingBottom();

        LogUtil.e(this, "lineList: "+lineList.size()  +  "  height: "+height);
        //告诉系统我要申请这么多的宽高
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        for (int i = 0; i < lineList.size(); i++) {

            Line line = lineList.get(i);//获取当前的line
            ArrayList<View> viewList = line.getViewList();//获取当前line的子view集合

            //每行的top是不断增加的
            if(i!=0){
                Line preLine = lineList.get(i-1);//获取上一行
                paddingTop += preLine.getHeight()+verticalSpacing;
            }

            //获取每行的留白
            float remainSpace = getMeasuredWidth()-getPaddingLeft()-getPaddingRight()-line.getWidth();
            //计算出每个子view平分到的留白
            float perSpace = remainSpace/viewList.size();

            for (int j = 0; j < viewList.size(); j++) {
                View childView = viewList.get(j);
                //将每个子view得到的留白重新添加到自己的宽度，然后重新测量
                int widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childView.getMeasuredWidth()+perSpace), MeasureSpec.EXACTLY);
                childView.measure(widthMeasureSpec, 0);

                if(j==0){
                    //如果是第一个子view，则靠左边摆放
                    childView.layout(paddingLeft, paddingTop, paddingLeft+childView.getMeasuredWidth(), paddingTop+childView.getMeasuredHeight());
                }else {
                    //后面子view，总是比前一个要多个horizontalSpacing
                    View preView = viewList.get(j-1);//获取前一个子view
                    int left = preView.getRight()+horizontalSpacing;//前一个view的right+horizontalSpacing
                    childView.layout(left, preView.getTop(), left+childView.getMeasuredWidth(), preView.getBottom());
                }
            }

        }
    }

    public void setVerticalSpacing(int verticalSpacing){
        this.verticalSpacing = verticalSpacing;
    }

    public int getHorizontalSpacing() {
        return horizontalSpacing;
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
    }

    /**
     * 用来保存每行的子view，代表一行
     */
    class Line{
        ArrayList<View> viewList;//用于记录行内所有的子view
        private int width;//表示所有子view的宽加上中间的horizontalSpacing
        private int height;//记录自身的高度
        public Line(){
            viewList = new ArrayList<View>();
        }
        /**
         * 保存子view到viewList中
         */
        public void addView(View view){
            if(!viewList.contains(view)){
                viewList.add(view);
            }

            //更新width和height
            if(viewList.size()==1){
                //只有1个子veiw，则line的width就是当前子view的宽
                width = view.getMeasuredWidth();
            }else {
                //有多个子veiw的时候，除了加上当前view的宽，还要加上horizontalSpacing
                width += view.getMeasuredWidth()+getHorizontalSpacing();
            }
            //总是取子veiw中最高的作为自己高度
            height = Math.max(height, view.getMeasuredHeight());
        }
        public int getWidth(){
            return width;
        }
        public int getHeight(){
            return height;
        }
        public ArrayList<View> getViewList(){
            return viewList;
        }
    }
}
