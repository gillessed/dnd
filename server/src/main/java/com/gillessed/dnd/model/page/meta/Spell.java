package com.gillessed.dnd.model.page.meta;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
@JsonSerialize
public interface Spell {
    String getName();
    String getLevel();
    String getType();
    String getTypeLevel();
    String getCastingTime();
    String getManaConsumption();
    String getRange();
    @Nullable
    String getArea();
    @Nullable
    String getDuration();
    String getSavingThrow();
    String getDescription();
    String getEffect();
    @Nullable
    String getNotes();
    @Nullable
    String getOrigin();

    class Builder extends ImmutableSpell.Builder {}
}

/*


Name: Flare
Level: 1
Type: Manipulator
Type Level: 1
Casting Time: 1 standard action
Mana Consumption: 23
Range: 50 ft.
Area: The caster and all allies within a 50-ft. burst, centered on the caster
Duration: 1 min./level
Saving Throw: none
Spell Resistance: yes (harmless)
Description: Bless fills your allies with courage.
Effect: Each ally gains a +1 morale bonus on attack rolls and on saving throws against fear effects.
Notes:
 */