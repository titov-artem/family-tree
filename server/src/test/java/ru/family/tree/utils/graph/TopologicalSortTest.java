package ru.family.tree.utils.graph;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author scorpion@yandex-team on 13.04.15.
 */
@SuppressWarnings("MessageMissingOnJUnitAssertion")
public class TopologicalSortTest {

  @Test
  public void testSort() throws Exception {
    final Graph<Integer> g = new Graph<>();
    g.addEdge(new Edge<>(new Vertex<>(1), new Vertex<>(4)));
    g.addEdge(new Edge<>(new Vertex<>(4), new Vertex<>(2)));
    g.addEdge(new Edge<>(new Vertex<>(4), new Vertex<>(3)));
    g.addEdge(new Edge<>(new Vertex<>(3), new Vertex<>(2)));

    final List<Vertex<Integer>> sorted = TopologicalSort.sort(g);
    final List<Integer> expectedOrder = Lists.newArrayList(1, 4, 3, 2);
    for (int i = 0; i < expectedOrder.size(); i++) {
      final Integer vValue = expectedOrder.get(i);
      final Vertex<Integer> v = sorted.get(i);
      assertThat(v.getValue(), is(vValue));
    }
  }
}