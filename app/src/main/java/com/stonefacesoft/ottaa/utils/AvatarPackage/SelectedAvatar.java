package com.stonefacesoft.ottaa.utils.AvatarPackage;

public class SelectedAvatar {
    private static String name="ic_avatar35";
    private static SelectedAvatar _SelectedAvatar;
    public synchronized static SelectedAvatar getInstance(){
        if(_SelectedAvatar == null){
            _SelectedAvatar = new SelectedAvatar();
        }
        return _SelectedAvatar;
    }

    public static void setName(String name) {
        SelectedAvatar.name = name;
    }

    public String getName() {
        return name;
    }
}
