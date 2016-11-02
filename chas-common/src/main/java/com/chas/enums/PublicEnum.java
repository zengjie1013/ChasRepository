package com.chas.enums;

/**
 * 通用枚举类（只针对只有"是"和"否"状态的枚举）
 *
 * @author Chas
 * @date 2016-11-02
 */
public enum PublicEnum {

    No(0,"否"),
    Yes(1,"是");

    private int value;
    private String text;

    PublicEnum(int value,String text){
        this.value = value;
        this.text = text;
    }

    public static PublicEnum fromValue(int value){
        for (PublicEnum status : PublicEnum.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("0-1 is a range of parameter('value')");
    }

    public static PublicEnum fromText(String text) {
        for (PublicEnum status : PublicEnum.values()) {
            if (status.text == text) {
                return status;
            }
        }
        throw new IllegalArgumentException("PublicEnum enum not is a range of parameter('text')");
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
