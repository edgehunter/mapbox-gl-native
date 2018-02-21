package com.mapbox.mapboxsdk.style.functions;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;

/**
 * Camera function. Functions that take camera properties as input (zoom for now)
 * <p>
 * Zoom functions allow the appearance of a map feature to change with map&#x27;s zoom level.
 * Zoom functions can be used to create the illusion of depth and control data density.
 * Each stop is an array with two elements: the first is a zoom level and the second is
 * a function output value.
 *
 * @param <O> the output type
 */
public class CameraFunction<O> extends Function<O> {

  /**
   * JNI Constructor
   */
  @Keep
  private CameraFunction(@NonNull Object expression) {
    super(expression);
  }
}
