#изучить как устроена стандартная коллекция ArrayList. Попрактиковаться в создании своей коллекции.

Написать свою реализацию ArrayList на основе массива. class DIYarrayList implements List{...}

Проверить, что на ней работают методы из java.util.Collections: Collections.addAll(Collection<? super T> c, T... elements) Collections.static void copy(List<? super T> dest, List<? extends T> src) Collections.static void sort(List list, Comparator<? super T> c)

Проверяйте на коллекциях с 20 и больше элементами.
DIYarrayList должен имплементировать ТОЛЬКО ОДИН интерфейс - List.
Если метод не имплементирован, то он должен выбрасывать исключение UnsupportedOperationException.
