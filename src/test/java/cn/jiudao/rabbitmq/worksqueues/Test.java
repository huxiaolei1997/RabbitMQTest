package cn.jiudao.rabbitmq.worksqueues;

import java.util.Collections;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Test
 *
 * version 1.0
 *
 * @create 2018-07-17 15:33
 *
 * @copyright huxiaolei1997@gmail.com
 */
public class Test {
    public static void main(String[] args) {
        SortedSet<Long> sortedSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
        for (long i = 0; i < 10; i++) {
            sortedSet.add(i);
        }

        sortedSet.headSet(Long.valueOf(6)).clear();
        Iterator<Long> iterator = sortedSet.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
