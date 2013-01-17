package server.services.message.address;

import server.services.message.Abonent;

public interface AddressService {
	public void registrateAbonent(String abonentName, Abonent abonent);
	public Address getAbonentAddress(String abonentName);
}
