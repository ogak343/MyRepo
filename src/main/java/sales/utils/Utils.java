package sales.utils;

import sales.dto.UserSpecificationRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static Map<String, Object> getSpecs(UserSpecificationRequest request) {
        Map<String, Object> specs = new HashMap<>();
        Arrays.stream(request.getClass().getDeclaredFields()).forEach(f -> {
            f.setAccessible(true);
            try {
                specs.put(f.getName(), f.get(request));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        return specs;
    }
}
