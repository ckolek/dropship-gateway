package me.kolek.ecommerce.dsgw.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;

public class AuthScopeSet implements Set<AuthScope> {

  private final Node root = new Node(null, null);

  @Override
  public int size() {
    return root.size();
  }

  @Override
  public boolean isEmpty() {
    return root.isEmpty();
  }

  @Override
  public Iterator<AuthScope> iterator() {
    return new AuthScopeIterator(root);
  }

  @Override
  public Object[] toArray() {
    return StreamSupport.stream(spliterator(), false).toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return StreamSupport.stream(spliterator(), false)
        .toArray(size -> (T[]) Arrays.copyOf(a, size, a.getClass()));
  }

  @Override
  public boolean add(AuthScope scope) {
    return root.add(scope.path().iterator(), scope.action());
  }

  @Override
  public boolean remove(Object o) {
    if (o instanceof AuthScope scope) {
      return root.remove(scope.path().iterator(), scope.action());
    }
    return false;
  }

  @Override
  public boolean contains(Object o) {
    if (o instanceof AuthScope scope) {
      return root.contains(scope.path().iterator(), scope.action());
    }
    return false;
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    for (Object o : c) {
      if (!contains(o)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean addAll(Collection<? extends AuthScope> c) {
    boolean allAdded = true;
    for (AuthScope scope : c) {
      allAdded &= add(scope);
    }
    return allAdded;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    Set<AuthScope> toRemove = new HashSet<>();
    for (AuthScope scope : this) {
      if (!c.contains(scope)) {
        toRemove.add(scope);
      }
    }
    return removeAll(toRemove);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    boolean removedAny = false;
    for (Object o : c) {
      removedAny |= remove(o);
    }
    return removedAny;
  }

  @Override
  public void clear() {
    root.clear();
  }

  public static AuthScopeSet copyOfValues(Collection<String> scopes) {
    return scopes.stream().map(AuthScope::parse)
        .collect(Collectors.toCollection(AuthScopeSet::new));
  }

  public static AuthScopeSet copyOf(Collection<AuthScope> scopes) {
    AuthScopeSet copy = new AuthScopeSet();
    copy.addAll(scopes);
    return copy;
  }

  @RequiredArgsConstructor
  private static class Node {
    private final Node parent;
    private final String value;
    private final Map<String, Node> nodes = new HashMap<>();
    private final Set<String> actions = new HashSet<>();

    public List<String> getPath() {
      List<String> path = new ArrayList<>();
      addPath(path);
      return path;
    }

    private void addPath(List<String> path) {
      if (parent != null) {
        parent.addPath(path);
      }
      if (value != null) {
        path.add(value);
      }
    }

    public boolean hasActions() {
      return !actions.isEmpty();
    }

    public int size() {
      return nodes.values().stream().mapToInt(Node::size).sum() + actions.size();
    }

    public boolean isEmpty() {
      return nodes.values().stream().allMatch(Node::isEmpty) && actions.isEmpty();
    }

    public boolean add(Iterator<String> path, String action) {
      if (path.hasNext()) {
        return nodes.computeIfAbsent(path.next(), value -> new Node(this, value)).add(path, action);
      } else {
        return actions.add(action);
      }
    }

    private Optional<Node> getNode(String value) {
      return Optional.ofNullable(nodes.get(value));
    }

    public boolean contains(Iterator<String> path, String action) {
      if (path.hasNext()) {
        return getNode(path.next())
            .or(() -> getNode(AuthScope.ANY))
            .map(node -> node.contains(path, action))
            .orElse(Boolean.FALSE);
      } else {
        return actions.contains(action) || actions.contains(AuthScope.ANY);
      }
    }

    public boolean remove(Iterator<String> path, String action) {
      if (path.hasNext()) {
        return getNode(path.next()).map(node -> node.remove(path, action)).orElse(Boolean.FALSE);
      } else {
        return actions.remove(action);
      }
    }

    public void clear() {
      nodes.clear();
      actions.clear();
    }
  }

  private static class NodeIterator implements Iterator<Node> {

    private final Deque<Iterator<Node>> iterators = new LinkedList<>();

    public NodeIterator(Node node) {
      addIterator(node);
    }

    private void addIterator(Node node) {
      Collection<Node> nodes = node.nodes.values();
      if (!nodes.isEmpty()) {
        iterators.push(nodes.iterator());
      }
    }

    @Override
    public boolean hasNext() {
      while (!iterators.isEmpty() && !iterators.peek().hasNext()) {
        iterators.pop();
      }
      return !iterators.isEmpty();
    }

    @Override
    public Node next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      Node node = iterators.peek().next();
      addIterator(node);
      return node;
    }

    @Override
    public void remove() {
      Optional.ofNullable(iterators.peek()).ifPresent(Iterator::remove);
    }
  }

  private static class AuthScopeIterator implements Iterator<AuthScope> {
    private final NodeIterator nodeIterator;
    private Node node;
    private Iterator<String> actionIterator;

    public AuthScopeIterator(Node node) {
      this.nodeIterator = new NodeIterator(node);
    }

    @Override
    public boolean hasNext() {
      if (node != null && actionIterator.hasNext()) {
        return true;
      }
      while (nodeIterator.hasNext()) {
        Node node = nodeIterator.next();
        if (node.hasActions()) {
          this.node = node;
          this.actionIterator = node.actions.iterator();
          return true;
        }
      }
      return false;
    }

    @Override
    public AuthScope next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      return new AuthScope(actionIterator.next(), node.getPath());
    }

    @Override
    public void remove() {
      Optional.ofNullable(actionIterator).ifPresent(Iterator::remove);
    }
  }
}
