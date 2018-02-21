package com.mapbox.mapboxsdk.style.functions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mapbox.mapboxsdk.style.layers.PropertyValue;

/**
 * Source functions take Feature property names as input.
 * <p>
 * Source functions allow the appearance of a map feature to change with
 * its properties. Source functions can be used to visually differentiate
 * types of features within the same layer or create data visualizations.
 * Each stop is an array with two elements, the first is a property input
 * value and the second is a function output value. Note that support for
 * property functions is not available across all properties and platforms
 * at this time.
 *
 * @param <O> the output type
 */
public class SourceFunction<O> extends Function<O> {

  private final String property;
  private PropertyValue<O> defaultValue;

  /**
   * JNI Constructor
   */
  private SourceFunction(@Nullable O defaultValue, @NonNull String property, @NonNull Object expression) {
    super(expression);
    this.property = property;
    this.defaultValue = defaultValue != null ? new PropertyValue<>(property, defaultValue) : null;
  }

  /**
   * INTERNAL USAGE ONLY
   *
   * @return The feature property name
   */
  public String getProperty() {
    return property;
  }

  /**
   * Set the default value
   *
   * @param defaultValue the default value to use when no other applies
   * @return this (for chaining)
   */
  public SourceFunction<O> withDefaultValue(PropertyValue<O> defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }

  /**
   * @return the defaultValue
   */
  @Nullable
  public PropertyValue<O> getDefaultValue() {
    return defaultValue;
  }

}
