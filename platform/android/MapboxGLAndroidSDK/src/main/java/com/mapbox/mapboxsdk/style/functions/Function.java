package com.mapbox.mapboxsdk.style.functions;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.mapbox.mapboxsdk.style.expressions.Expression;

import java.util.ArrayList;
import java.util.List;

/**
 * Functions are used to change properties in relation to the state of the map.
 * <p>
 * The value for any layout or paint property may be specified as a function. Functions allow you to
 * make the appearance of a map feature change with the current zoom level and/or the feature's properties.
 *
 * @param <O> the target property's value type. Make sure it matches.
 * @see <a href="https://www.mapbox.com/mapbox-gl-style-spec/#types-function">The style specification</a>
 */
public class Function<O> {

  // Class definition //
  private JsonArray expression;

  /**
   * JNI Constructor for implementation classes
   */
  Function(Object object) {
    if (object instanceof JsonArray) {
      this.expression = (JsonArray) object;
    } else {
      throw new RuntimeException("Object is not a JsonArray!");
    }
  }

  public Expression getExpression() {
    return convert(this.expression);
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

  @Override
  public String toString() {
    return expression.toString();
  }
}
