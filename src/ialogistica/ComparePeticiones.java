package ialogistica;

import java.util.Comparator;

public class ComparePeticiones implements Comparator<Peticion> {

	@Override
	public int compare(Peticion arg0, Peticion arg1) {
		String arg0String = String.valueOf(arg0.getHoraEntrega());
		String arg1String = String.valueOf(arg1.getHoraEntrega());

		int result = arg0String.compareTo(arg1String);

		if (result == 0) {
			arg0String = String.valueOf(arg0.getCantidadPeticion());
			arg1String = String.valueOf(arg1.getCantidadPeticion());
			
			result = arg0String.compareTo(arg1String); 
		}
		
		return result;
	}

}
