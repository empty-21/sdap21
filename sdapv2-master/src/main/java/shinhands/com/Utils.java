package shinhands.com;

import io.smallrye.common.constraint.Nullable;

public class Utils {
    public static boolean isEmpty(@Nullable Object str) {
		return (str == null || "".equals(str));
	}
}
