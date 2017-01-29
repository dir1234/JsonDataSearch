package com.company.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.company.JsonSearchDataTestConfiguration;
import com.company.model.Customer;
import com.company.stubs.LoggerStub;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes=JsonSearchDataTestConfiguration.class)
public class AbstractNamedServiceTests {
	
	private AbstractNamedService<Customer> testSubject;
	private LoggerStub logStub = new LoggerStub();
	
	@Autowired
	@Qualifier("customerFile")
	private InputStream customerFile;
	
	@Autowired
	@Qualifier("missingFile")
	private InputStream missingFile;
	
	@Autowired
	@Qualifier("corruptFile")
	private InputStream corruptFile;
	
	@Before
	public void setUp() {
		testSubject = new AbstractNamedService<Customer>(logStub){
			@Override
			public List<Customer> filterByName(List<Customer> list, String name) {
				return null;
			}
			@Override
			public List<Customer> filterBy(List<Customer> list, String filterType, String filterValue) {
				return null;
			}
			@Override
			public List<Customer> sortBy(List<Customer> list, String sortType, String sortValue) {
				return null;
			}
			@Override
			protected Class<Customer> getModelClass() {
				return Customer.class;
			}
		};
	}
	
	@Test
	public void canLoadJsonSuccessfully() {
		testSubject.setFileStore(customerFile);
		
		assertNotNull(testSubject.list());
		assertEquals(3, testSubject.list().size());;
	}
	
	@Test
	public void handlesMissingFilesAppropriately() {
		testSubject.setFileStore(missingFile);
		assertEquals("Required file is missing: Unknown", logStub.getLastWarning());
	}
	
	@Test
	public void handlesCurruptDataFilesAppropriately() {
		testSubject.setFileStore(corruptFile);
		
		String errorString = (String) logStub.getLastError();
		assertTrue(errorString.contains("Exception occured during parsing! Datafile is likely corrupt: "));
	}
}
