package com.mapbox.mapboxsdk.style.layers;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.mapbox.mapboxsdk.exceptions.ConversionException;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.functions.Function;
import com.mapbox.mapboxsdk.utils.ColorUtils;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Properties for Layer
 */
public class PropertyValue<T> {

  public final String name;
  public final T value;

  /**
   * Not part of the public API.
   *
   * @param name  the property name
   * @param value the property value
   * @see PropertyFactory for construction of {@link PropertyValue}s
   */
  public PropertyValue(@NonNull String name, T value) {
    this.name = name;
    this.value = value;
  }

  /**
   * Returns if this is null
   *
   * @return true if this is null, false if not
   */
  public boolean isNull() {
    return value == null;
  }

  /**
   * Returns if this is a expression.
   *
   * @return true if this is a expression, false if not
   */
  public boolean isExpression() {
    return !isNull() && value instanceof JsonArray;
  }

  /**
   * Get the expression of the property.
   *
   * @return the property expression
   */
  @Nullable
  public Expression getExpression() {
    if (isExpression()) {
      return convert((JsonArray) value);
    } else {
      Timber.w("not a expression, try value");
      return null;
    }
  }

  /**
   * Returns if this is a value.
   *
   * @return true if is a value, false if not
   */
  public boolean isValue() {
    return !isNull() && !isExpression();
  }

  /**
   * Get the value of the property.
   *
   * @return the property value
   */
  @Nullable
  public T getValue() {
    if (isValue()) {
      // noinspection unchecked
      return value;
    } else {
      Timber.w("not a value, try function");
      return null;
    }
  }

  /**
   * Get the color int value of the property if the value is a color.
   *
   * @return the color int value of the property, null if not a color value
   */
  @ColorInt
  @Nullable
  public Integer getColorInt() {
    if (!isValue() || !(value instanceof String)) {
      Timber.e("%s is not a String value and can not be converted to a color it", name);
      return null;
    }

    try {
      return ColorUtils.rgbaToColor((String) value);
    } catch (ConversionException ex) {
      Timber.e("%s could not be converted to a Color int: %s", name, ex.getMessage());
      return null;
    }
  }

  /**
   * Get the string representation of a property value.
   *
   * @return the string representation
   */
  @Override
  public String toString() {
    return String.format("%s: %s", name, value);
  }

  private Expression convert(JsonArray jsonArray) {
    final String operator = jsonArray.get(0).getAsString();
    final List<Expression> arguments = new ArrayList<>();

    JsonElement jsonElement;
    JsonPrimitive jsonPrimitive;
    for (int i = 1; i < jsonArray.size(); i++) {
      jsonElement = jsonArray.get(i);
      if (jsonElement instanceof JsonArray) {
        arguments.add(convert((JsonArray) jsonElement));
      } else if (jsonElement instanceof JsonPrimitive) {
        jsonPrimitive = (JsonPrimitive) jsonElement;
        if (jsonPrimitive.isBoolean()) {
          arguments.add(new Expression.ExpressionLiteral(jsonElement.getAsBoolean()));
        } else if (jsonPrimitive.isNumber()) {
          arguments.add(new Expression.ExpressionLiteral(jsonElement.getAsFloat()));
        } else if (jsonPrimitive.isString()) {
          arguments.add(new Expression.ExpressionLiteral(jsonElement.getAsString()));
        } else {
          throw new RuntimeException("unsupported conversion");
        }
      } else {
        throw new RuntimeException("unsupported conversion");
      }
    }
    return new Expression(operator, arguments.toArray(new Expression[arguments.size()]));
  }
}
