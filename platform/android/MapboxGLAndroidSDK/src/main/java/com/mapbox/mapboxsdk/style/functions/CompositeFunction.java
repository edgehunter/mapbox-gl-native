package com.mapbox.mapboxsdk.style.functions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mapbox.mapboxsdk.style.layers.PropertyValue;

/**
 * Composite functions combine Camera and SourceFunctions.
 * <p>
 * Composite functions allow the appearance of a map feature to change with both its
 * properties and zoom. Each stop is an array with two elements, the first is an object
 * with a property input value and a zoom, and the second is a function output value. Note
 * that support for property functions is not yet complete.
 *
 * @param <O> the output type (the property type)
 */
public class CompositeFunction<O> extends Function<O> {

  private final String property;
  private PropertyValue<O> defaultValue;

  /**
   * JNI Constructor
   */
  private CompositeFunction(@Nullable O defaultValue, @NonNull String property, Object expression) {
    super(expression);
    this.defaultValue = new PropertyValue<>(property, defaultValue);
    this.property = property;
  }

  /**
   * @return the defaultValue
   */
  @Nullable
  public PropertyValue<O> getDefaultValue() {
    return defaultValue;
  }

  /**
   * INTERNAL USAGE ONLY
   *
   * @return the feature property name
   */
  public String getProperty() {
    return property;
  }

}
