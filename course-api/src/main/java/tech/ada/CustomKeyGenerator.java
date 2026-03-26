package tech.ada;

import io.quarkus.cache.CacheKeyGenerator;
import io.quarkus.logging.Log;

import java.lang.reflect.Method;

public class CustomKeyGenerator implements CacheKeyGenerator {

    @Override
    public Object generate(Method method, Object... methodParams) {

        CustomKey customAnnotation = method.getDeclaredAnnotation(CustomKey.class);
        if (customAnnotation == null) {
            throw new IllegalStateException("CustomKey method has no @CustomKey annotation");
        }

        String value = customAnnotation.value(); // "listaDeCursos"
        Log.info("Generating cache key for method " + method.getName() + " with value: " + value);

        return value;
    }
}
