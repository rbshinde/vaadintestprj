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

import com.gl.glopration.model.OprationBean;
import com.gl.glopration.model.ProvinceBean;
import com.gl.glopration.model.ReceiptCode;

/**
 * An in memory dummy "database" for the example purposes. In a typical Java app
 * this class would be replaced by e.g. EJB or a Spring based service class.
 * <p>
 * In demos/tutorials/examples, get a reference to this service class with
 * {@link OprationService#getInstance()}.
 */
@Service
public class OprationService {

	private static final Logger LOGGER = Logger.getLogger(OprationService.class.getName());

	private final HashMap<Long, OprationBean> oprationHashMap = new HashMap<>();
	public HashMap<Long, OprationBean> getOprationHashMap() {
		return oprationHashMap;
	}

	private long nextId = 0;

	private OprationService() {
	}

	/**
	 * @return a reference to an example facade for Customer objects.
	 */
	/**
	 * @return all available Customer objects.
	 */
	public synchronized List<OprationBean> findAll() {
		return findAll(null);
	}

	/**
	 * Finds all Customer's that match given filter.
	 *
	 * @param stringFilter
	 *            filter that returned objects should match or null/empty string if
	 *            all objects should be returned.
	 * @return list a Customer objects
	 */
	public synchronized List<OprationBean> findAll(String stringFilter) {
		ArrayList<OprationBean> arrayList = new ArrayList<>();
		for (OprationBean contact : oprationHashMap.values()) {
			try {
				boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
						|| contact.toString().toLowerCase().contains(stringFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(contact.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(OprationService.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<OprationBean>() {

			@Override
			public int compare(OprationBean o1, OprationBean o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		return arrayList;
	}

	/**
	 * Finds all Customer's that match given filter and limits the resultset.
	 *
	 * @param stringFilter
	 *            filter that returned objects should match or null/empty string if
	 *            all objects should be returned.
	 * @param start
	 *            the index of first result
	 * @param maxresults
	 *            maximum result count
	 * @return list a Customer objects
	 */
	public synchronized List<OprationBean> findAll(String stringFilter, int start, int maxresults) {
		ArrayList<OprationBean> arrayList = new ArrayList<>();
		for (OprationBean contact : oprationHashMap.values()) {
			try {
				boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
						|| contact.toString().toLowerCase().contains(stringFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(contact.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(OprationService.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<OprationBean>() {

			@Override
			public int compare(OprationBean o1, OprationBean o2) {
				return (int) (o2.getId() - o1.getId());
			}
		});
		int end = start + maxresults;
		if (end > arrayList.size()) {
			end = arrayList.size();
		}
		return arrayList.subList(start, end);
	}

	/**
	 * @return the amount of all customers in the system
	 */
	public synchronized long count() {
		return oprationHashMap.size();
	}

	/**
	 * Deletes a customer from a system
	 *
	 * @param value
	 *            the Customer to be deleted
	 */
	public synchronized void delete(Long value) {
		oprationHashMap.remove(value);
	}

	/**
	 * Persists or updates customer in the system. Also assigns an identifier for
	 * new Customer instances.
	 *
	 * @param entry
	 */
	public synchronized void save(OprationBean entry) {
		
		
		if (entry == null) {
			LOGGER.log(Level.SEVERE,
					"Customer is null. Are you sure you have connected your form to the application as described in tutorial chapter 7?");
			return;
		}
		ProvinceBean pBean = entry.getProvinceBean();
		if( pBean.getDLTCharge()!=null && pBean.getDLTCharge() != 0 )
		{
			entry.setDLTCharge(pBean.getDLTCharge());
		}	
		if( pBean.getWage()!=null && pBean.getWage() != 0 )
		{
			entry.setWage(pBean.getWage());
		}	
		if(entry.getPrice()!=null && entry.getPrice() !=0 && entry.getPrice() >= 1000) {
			entry.setWage(0.00);
		}else if(entry.getPrice()!=null && entry.getPrice() !=0 && entry.getPrice() >= 2000){
			double dicountPrice = entry.getPrice()*5/100;
			entry.setWage(entry.getPrice()-dicountPrice);
		}
		
		if (entry.getId() == null) {
			entry.setId(nextId++);
		}
		try {
			entry = (OprationBean) entry.clone();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		oprationHashMap.put(entry.getId(), entry);
	}

	/**
	 * Sample data generation
	 */
	@PostConstruct
	public void ensureTestData() {
		if (findAll().isEmpty()) {
			final String[] names = new String[] { "001 oprationCode1 receiptCode1 500 0.00 0.00",
					"002 oprationCode2 receiptCode3 100 0.00 0.00", "003 oprationCode3 receiptCode3 200 0.00 0.00",
					"004 oprationCode4 receiptCode3 300 0.00 0.00", "005 oprationCode5 receiptCode3 400 0.00 0.00" };
			Random r = new Random(0);
			for (String name : names) {
				String[] split = name.split(" ");
				OprationBean c = new OprationBean();

				c.setOprationCode(split[0]);
				c.setOprationDesciption(split[1]);
				c.setReceiptCode(ReceiptCode.values()[r.nextInt(ReceiptCode.values().length)]);
				c.setPrice(10.0);
				c.setDLTCharge(0.00);
				c.setWage(0.00);
				c.setProvinceBean(new ProvinceBean());
				save(c);
			}
		}
	}
}