package frigengine.tests.util;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import frigengine.entities.Entity;
import frigengine.exceptions.IDableException;
import frigengine.util.IDableCollection;

public class IDableCollectionTest {
	private IDableCollection<Entity> col = null;
	private Entity ent = new Entity("e1");

	@Before
	public void init() {
		col = new IDableCollection<Entity>();
	}

	@Test
	public void testAdd() {
		Entity e = null;
		for (int i = 0; i < 1000; i++) {
			e = new Entity("e" + i);
			col.add(e);
		}

		Assert.assertEquals(1000, col.size());
	}

	@Test(expected = IDableException.class)
	public void testRepeatAdd() {
		col.add(ent);
		col.add(ent);
	}

	@Test
	public void testGet() {
		col.add(ent);
		Assert.assertEquals(ent, col.get(ent.getID()));
	}

	@Test
	public void testBigGet() {
		Entity e = null;
		Entity e2 = null;
		for (int i = 0; i < 1000; i++) {
			e = new Entity("e" + i);
			col.add(e);

			if (i == 539) {
				e2 = e;
			}
		}

		Assert.assertEquals(e2, col.get(e2.getID()));
	}

	@Test(expected = IDableException.class)
	public void testBadGet() {
		col.add(ent);
		col.get("e2");
	}

	@Test
	public void testContains() {
		col.add(ent);

		Assert.assertEquals(true, col.contains(ent.getID()));
	}

	@Test
	public void testBigContains() {
		Entity e = null;
		for (int i = 0; i < 1000; i++) {
			e = new Entity("e" + i);
			col.add(e);
		}

		for (int i = 999; i >= 0; i--) {
			Assert.assertEquals(true, col.contains("e" + i));
		}
	}

	@Test
	public void testBadContains() {
		col.add(ent);

		Assert.assertEquals(false, col.contains("e2"));
	}

	@Test
	public void testBigBadContains() {
		Entity e = null;
		for (int i = 0; i < 1000; i++) {
			e = new Entity("e" + i);
			col.add(e);
		}

		for (int i = 0; i < 1000; i++) {
			Assert.assertEquals(false, col.contains("r" + 1));
		}
	}

	@Test
	public void testIsEmptyFalse() {
		col.add(ent);
		Assert.assertEquals(false, col.isEmpty());
	}

	@Test
	public void testIsEmptyTrue() {
		Assert.assertEquals(true, col.isEmpty());
	}

	@Test
	public void testRemove() {
		col.add(ent);
		col.remove(ent.getID());
	}

	@Test
	public void testBadRemove() {
		col.add(ent);
		col.remove("e2");
	}

	@Test
	public void testSize() {
		col.add(ent);
		Assert.assertEquals(1, col.size());
	}

	@Test
	public void testBigSize() {
		Entity e = null;
		for (int i = 0; i < 1000; i++) {
			e = new Entity("e" + i);
			col.add(e);
		}

		Assert.assertEquals(1000, col.size());
	}

	@Test
	public void testZeroSize() {
		Assert.assertEquals(0, col.size());
	}

}
