package com.stonefacesoft.ottaa.utils.Games;

public class TellAStoryUtils {
    private int pictoPosition;
    private static TellAStoryUtils _TellAStoryUtils;
    public static synchronized TellAStoryUtils getInstance(){
        if(_TellAStoryUtils == null)
            _TellAStoryUtils = new TellAStoryUtils();
        return _TellAStoryUtils;
    };

    public enum FilterGroups{
        Position0(new int[]{2,17,3,12}),Position1(new int[]{6,1,16,20}),Position2(new int[]{0,24}),Position3(new int[]{4,13,21});
        private final int[] options;
        FilterGroups(int[] list){
            this.options = list;
        }

        public int[] getOptions() {
            return options;
        }
    };

    public FilterGroups getItem(){
        if(pictoPosition>FilterGroups.values().length-1)
            pictoPosition = FilterGroups.values().length-1;
        return FilterGroups.values()[pictoPosition];
    }

    public void setPictoPosition(int pictoPosition) {
        this.pictoPosition = pictoPosition;
    }

    public int getPictoPosition() {
        return pictoPosition;
    }
}
