package ehnetwork.core.pet;

import org.bukkit.entity.EntityType;

import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.pet.repository.token.ClientPetToken;
import ehnetwork.core.pet.repository.token.PetToken;

public class PetClient
{
    private NautHashMap<EntityType, String> _pets;
    private int _petNameTagCount;
    
    public void Load(ClientPetToken token)
    {
	    _pets = new NautHashMap<EntityType, String>();
	    
	    for (PetToken petToken : token.Pets)
	    {
	    	if (petToken.PetName == null)
	    		petToken.PetName = Enum.valueOf(EntityType.class, petToken.PetType).getName();
	    	
	    	_pets.put(Enum.valueOf(EntityType.class, petToken.PetType), petToken.PetName);
	    }
	    
	    _petNameTagCount = Math.max(0, token.PetNameTagCount);
    }
    
	public NautHashMap<EntityType, String> GetPets()
	{
		return _pets;
	}

	public Integer GetPetNameTagCount()
	{
		return _petNameTagCount;
	}

	public void SetPetNameTagCount(int count)
	{
		_petNameTagCount = count;
	}
}
