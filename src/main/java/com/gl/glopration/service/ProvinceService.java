package com.gl.glopration.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.gl.glopration.model.ProvinceAreas;
import com.gl.glopration.model.ProvinceBean;

@Service
public class ProvinceService {

	private static ProvinceService instance;
	private static final Logger LOGGER = Logger.getLogger(ProvinceService.class.getName());

	private final HashMap<Long, ProvinceBean> provinceMap = new HashMap<>();
	public HashMap<Long, ProvinceBean> getProvinceMap() {
		return provinceMap;
	}

	private long nextId = 0;

	@PostConstruct
	private void ensureTestData() {
		if (findAll().isEmpty()) {
			final String[] names = new String[] { "333 111 3333", "555 100 200", "999 1000 1200", "98569 220 111", };
			Random r = new Random(0);
			for (String name : names) {
				String[] split = name.split(" ");
				ProvinceBean c = new ProvinceBean();

				c.setProvinceAreas(ProvinceAreas.values()[r.nextInt(ProvinceAreas.values().length)]);
				c.setDLTCharge(0.00);
				c.setWage(0.00);
				save(c);
			}
		}
	}

	public synchronized List<ProvinceBean> findAll() {
		return findAll(null);
	}

	private List<ProvinceBean> findAll(String stringFilter) {
		ArrayList<ProvinceBean> arrayList = new ArrayList<>();
		for (ProvinceBean contact : provinceMap.values()) {
			try {
				boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
						|| contact.toString().toLowerCase().contains(stringFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(contact.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(ProvinceService.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<ProvinceBean>() {

			@Override
			public int compare(ProvinceBean o1, ProvinceBean o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		return arrayList;
	}

	public synchronized long count() {
		return provinceMap.size();
	}

	public synchronized void delete(Long value) {
		provinceMap.remove(value);
	}

	/**
	 * Persists or updates customer in the system. Also assigns an identifier for
	 * new Customer instances.
	 *
	 * @param entry
	 */
	public synchronized void save(ProvinceBean entry) {
		if (entry == null) {
			LOGGER.log(Level.SEVERE,
					"Customer is null. Are you sure you have connected your form to the application as described in tutorial chapter 7?");
			return;
		}
		if (entry.getId() == null) {
			entry.setId(nextId++);
		}
		try {
			entry = (ProvinceBean) entry.clone();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		provinceMap.put(entry.getId(), entry);
	}
}
