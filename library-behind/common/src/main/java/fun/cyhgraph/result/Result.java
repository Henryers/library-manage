package fun.cyhgraph.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    private Integer code;
    private String msg;
    private T data;

    /**
     * 注意：静态方法是属于类的而不是对象的，因此才可以Result.success()直接 “类” 调用方法！！！
     * 而且，由于声明了static，表示下面的方法不属于类Rusult<T>，即相当于没声明
     * 因此static方法后还要再加一个<T>表示该方法是一个泛型方法
     * @return
     * @param <T>
     */
    public static <T> Result<T> success(){
        Result<T> result = new Result<>();
        result.code = 0;
        return result;
    }

    public static <T> Result<T> success(T data){
        Result<T> result = new Result<>();
        result.code = 0;
        result.data = data;
        return result;
    }

    public static <T> Result<T> error(String msg){
        Result<T> result = new Result<>();
        result.code = 1;
        result.msg = msg;
        return result;
    }
}

