package ehnetwork.core.pet;

import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.pet.repository.PetRepository;
import ehnetwork.core.pet.types.Elf;
import ehnetwork.core.pet.types.Pumpkin;

public class PetFactory
{
	private PetRepository _repository;
	private NautHashMap<EntityType, Pet> _pets;
	private NautHashMap<Material, PetExtra> _petExtras;
	
	public PetFactory(PetRepository repository)
	{
		_repository = repository;
		_pets = new NautHashMap<EntityType, Pet>();
		_petExtras = new NautHashMap<Material, PetExtra>();
		
		CreatePets();
		CreatePetExtras();
	}
	
	private void CreatePets()
	{
		_pets.put(EntityType.ZOMBIE, new Pumpkin());
		_pets.put(EntityType.VILLAGER, new Elf());
		_pets.put(EntityType.PIG, new Pet("Pig", EntityType.PIG, 5000));
		_pets.put(EntityType.SHEEP, new Pet("Sheep", EntityType.SHEEP, 3000));
		_pets.put(EntityType.COW, new Pet("Cow", EntityType.COW, 2000));
		_pets.put(EntityType.CHICKEN, new Pet("Chicken", EntityType.CHICKEN, 7000));
		_pets.put(EntityType.WOLF, new Pet("Dog", EntityType.WOLF, 8000));
		_pets.put(EntityType.OCELOT, new Pet("Cat", EntityType.OCELOT, 6000));
		_pets.put(EntityType.MUSHROOM_COW, new Pet("Mooshroom", EntityType.MUSHROOM_COW, 5000));
		_pets.put(EntityType.WITHER, new Pet("Widder", EntityType.WITHER, -1));
	}

	private void CreatePetExtras()
	{
		_petExtras.put(Material.SIGN, new PetExtra("Name Tag", Material.NAME_TAG, 100));
	}
	
	public Collection<Pet> GetPets()
	{
		return _pets.values();
	}
	
	public Collection<PetExtra> GetPetExtras()
	{
		return _petExtras.values();
	}
	
	public Collection<PetExtra> GetPetExtraBySalesId(int salesId)
	{
		return _petExtras.values();
	}
}
