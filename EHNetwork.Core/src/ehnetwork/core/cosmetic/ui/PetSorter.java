package ehnetwork.core.cosmetic.ui;

import java.util.Comparator;

import ehnetwork.core.pet.Pet;

public class PetSorter implements Comparator<Pet>
{	
	public int compare(Pet a, Pet b) 
	{
		if (a.GetPetType().getTypeId() < b.GetPetType().getTypeId())
			return -1;

		return 1;
	}
}