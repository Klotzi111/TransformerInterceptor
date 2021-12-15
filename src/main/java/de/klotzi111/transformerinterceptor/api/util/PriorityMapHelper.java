package de.klotzi111.transformerinterceptor.api.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;

public class PriorityMapHelper {

	public static enum PrioritySearchDirection {
		HIGHER(1),
		LOWER(-1);

		public final int priorityIncrement;

		private PrioritySearchDirection(int priorityIncrement) {
			this.priorityIncrement = priorityIncrement;
		}

		private boolean continueSearch(int nextUsedPriority, int priority) {
			if (priorityIncrement < 0) {
				return nextUsedPriority >= priority;
			} else if (priorityIncrement > 0) {
				return nextUsedPriority <= priority;
			}
			return false;
		}

		private <T> Int2ObjectSortedMap<T> subMapNextPriority(Int2ObjectSortedMap<T> map, int priority) {
			if (priorityIncrement < 0) {
				return map.tailMap(priority);
			} else if (priorityIncrement > 0) {
				return map.subMap(Integer.MAX_VALUE, priority - 1);
			}
			return map.subMap(priority, priority - 1);
		}

		private <T> int getClosestPriorityInSubMap(Int2ObjectSortedMap<T> map) {
			if (priorityIncrement < 0) {
				return map.firstIntKey();
			} else if (priorityIncrement > 0) {
				return map.lastIntKey();
			}
			// firstIntKey and lastIntKey should be same since the map only has one element
			return map.firstIntKey();
		}

		public int next(int priority) {
			return priority + priorityIncrement;
		}
	}

	/**
	 * If the {@code priority} is already present in the map the next unused priority according to {@code searchDir} will be returned
	 *
	 * @param map
	 * @param priority
	 * @param searchDir
	 * @param <T>
	 * @return
	 */
	private static <T> int findNextUnusedPriority(Int2ObjectSortedMap<T> map, int priority, PrioritySearchDirection searchDir) {
		if (!map.containsKey(priority)) {
			return priority;
		}
		int nextUsedPriority = priority;
		while (searchDir.continueSearch(nextUsedPriority, priority)) {
			priority = searchDir.next(priority);
			map = searchDir.subMapNextPriority(map, priority);
			if (map.isEmpty()) {
				break;
			}
			nextUsedPriority = searchDir.getClosestPriorityInSubMap(map);
		}
		return priority;
	}

	/**
	 * Adds the value to the map with the given {@code priority}. If the {@code priority} is already present in the map the next unused priority according to {@code searchDir} will be used
	 *
	 * @param <T>
	 * @param map
	 * @param priority
	 * @param value
	 * @param searchDir
	 */
	public static <T> void addSafe(Int2ObjectSortedMap<T> map, int priority, T value, PrioritySearchDirection searchDir) {
		if (value == null) {
			throw new IllegalArgumentException("value is null");
		}
		map.put(findNextUnusedPriority(map, priority, searchDir), value);
	}

	/**
	 * {@link #addSafe(Int2ObjectSortedMap, int, Object, PrioritySearchDirection)} with {@code searchDir} defaulted to {@code PrioritySearchDirection.LOWER}
	 *
	 * @param <T>
	 * @param map
	 * @param priority
	 * @param value
	 */
	public static <T> void addSafe(Int2ObjectSortedMap<T> map, int priority, T value) {
		addSafe(map, priority, value, PrioritySearchDirection.LOWER);
	}

	/**
	 * Puts the {@code value} at {@code priority}. If the {@code priority} is already present in the map the old value is put at the next priority according to {@code dir}.
	 * This is continued until the put did replace nothing
	 *
	 * @param <T>
	 * @param map
	 * @param priority
	 * @param value
	 * @param dir
	 */
	public static <T> void putAndMoveOthers(Int2ObjectSortedMap<T> map, int priority, T value, PrioritySearchDirection dir) {
		if (value == null) {
			throw new IllegalArgumentException("value is null");
		}
		T replaced = map.put(priority, value);
		while (replaced != null) {
			priority = dir.next(priority);
			replaced = map.put(priority, value);
		}
	}

}
