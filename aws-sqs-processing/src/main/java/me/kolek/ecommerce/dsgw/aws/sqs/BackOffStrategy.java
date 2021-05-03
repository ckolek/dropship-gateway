package me.kolek.ecommerce.dsgw.aws.sqs;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;

public abstract class BackOffStrategy {

  public abstract int calculateVisibilityTimeout(int receiveCount);

  @RequiredArgsConstructor
  public static class Constant extends BackOffStrategy {

    private static final Pattern PATTERN = Pattern.compile("const (\\d+)");
    private final int value;

    @Override
    public int calculateVisibilityTimeout(int receiveCount) {
      return value;
    }

    public static Optional<BackOffStrategy> parse(String input) {
      var matcher = PATTERN.matcher(input);
      if (matcher.matches()) {
        int value = Integer.parseInt(matcher.group(1));
        return Optional.of(new Constant(value));
      }
      return Optional.empty();
    }
  }

  @RequiredArgsConstructor
  public static class Linear extends BackOffStrategy {

    private static final Pattern PATTERN = Pattern.compile("lin (\\d+)( \\d+)?");

    private final int m;
    private final int b;

    @Override
    public int calculateVisibilityTimeout(int receiveCount) {
      return m * receiveCount + b;
    }

    public static Optional<BackOffStrategy> parse(String input) {
      var matcher = PATTERN.matcher(input);
      if (matcher.matches()) {
        int m = Integer.parseInt(matcher.group(1));
        int b = matcher.groupCount() > 1 ? Integer.parseInt(matcher.group(2).trim()) : 0;
        return Optional.of(new Linear(m, b));
      }
      return Optional.empty();
    }
  }

  @RequiredArgsConstructor
  public static class Quadratic extends BackOffStrategy {

    private static final Pattern PATTERN = Pattern.compile("quad (\\d+)");

    private final int n;

    @Override
    public int calculateVisibilityTimeout(int receiveCount) {
      return (int) Math.pow(receiveCount, n);
    }

    public static Optional<BackOffStrategy> parse(String input) {
      var matcher = PATTERN.matcher(input);
      if (matcher.matches()) {
        int n = Integer.parseInt(matcher.group(1));
        return Optional.of(new Quadratic(n));
      }
      return Optional.empty();
    }
  }

  @RequiredArgsConstructor
  public static class Exponential extends BackOffStrategy {

    private static final Pattern PATTERN = Pattern.compile("exp (\\d+)");

    private final int e;

    @Override
    public int calculateVisibilityTimeout(int receiveCount) {
      return (int) Math.pow(e, receiveCount);
    }

    public static Optional<BackOffStrategy> parse(String input) {
      var matcher = PATTERN.matcher(input);
      if (matcher.matches()) {
        int e = Integer.parseInt(matcher.group(1));
        return Optional.of(new Exponential(e));
      }
      return Optional.empty();
    }
  }

  @RequiredArgsConstructor
  public static class Logarithmic extends BackOffStrategy {

    private static final Pattern PATTERN = Pattern.compile("log");

    @Override
    public int calculateVisibilityTimeout(int receiveCount) {
      return (int) Math.log(receiveCount);
    }

    public static Optional<BackOffStrategy> parse(String input) {
      var matcher = PATTERN.matcher(input);
      if (matcher.matches()) {
        return Optional.of(new Logarithmic());
      }
      return Optional.empty();
    }
  }

  @RequiredArgsConstructor
  public static class Custom extends BackOffStrategy {

    private static final Pattern PATTERN = Pattern.compile("cust(( \\d+)+)");

    private final int[] values;

    @Override
    public int calculateVisibilityTimeout(int receiveCount) {
      return values[Math.min(receiveCount - 1, values.length)];
    }

    public static Optional<BackOffStrategy> parse(String input) {
      var matcher = PATTERN.matcher(input);
      if (matcher.matches()) {
        int[] values = Stream.of(matcher.group(1).split(" ")).mapToInt(Integer::parseInt).toArray();
        return Optional.of(new Custom(values));
      }
      return Optional.empty();
    }
  }

  public static Optional<BackOffStrategy> valueOf(String input) {
    if (input == null) {
      return Optional.empty();
    }
    return Constant.parse(input)
        .or(() -> Linear.parse(input))
        .or(() -> Quadratic.parse(input))
        .or(() -> Exponential.parse(input))
        .or(() -> Logarithmic.parse(input))
        .or(() -> Custom.parse(input));
  }
}
