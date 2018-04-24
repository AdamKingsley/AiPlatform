package cn.edu.nju.software.common.shiro;

/**
 * Created by mengf on 2018/4/24 0024.
 */
public enum StateEnum {
    NOT_ACTIVE(0), ACTIVED(1), BLOCKED(2);

    private Integer state;

    StateEnum(Integer state) {
        this.state = state;
    }

    public int getState() {
        return this.state;
    }
}
