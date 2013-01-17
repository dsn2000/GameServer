package server.services.message.address;

import java.util.HashMap;
import java.util.Map;

import server.services.message.Abonent;

public class AddressServiceImpl implements AddressService {
	private static AddressServiceImpl addressService = null;
	private Map<String, Abonent> abonents = new HashMap<String, Abonent>();
		
	public void registrateAbonent(String abonentName, Abonent abonent) {
		abonents.put(abonentName, abonent);
	}
	
	public Address getAbonentAddress(String abonentName) {
		Abonent abonent = abonents.get(abonentName);
		if (abonent == null) {
			return null;
		}
		return abonent.getAddress();
	}
	
	private AddressServiceImpl() {}
	
	public static AddressService getInstance() {
		if (addressService == null) {
			addressService = new AddressServiceImpl();
		} 
		return addressService;		
	}
}
