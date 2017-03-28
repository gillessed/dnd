package com.gillessed.dnd.page.compiler.impl;

import com.gillessed.dnd.model.page.WikiObject;
import com.gillessed.dnd.model.page.meta.Spell;
import com.gillessed.dnd.model.page.objects.WikiMeta;
import com.gillessed.dnd.page.compiler.AbstractObjectCompiler;
import com.gillessed.dnd.page.compiler.ObjectCompiler;
import com.gillessed.dnd.page.compiler.exception.CompilerException;
import com.gillessed.dnd.page.parser.Element;

import java.util.List;

public class MetaCompiler extends AbstractObjectCompiler implements ObjectCompiler {
    @Override
    public WikiObject compileObject(List<Element> elements) throws CompilerException {
        checkElementsNotEmpty(elements);
        String description = parseAttribute("desc", elements.get(0));
        String status = parseAttribute("status", elements.get(0));
        String typeString = null;
        try {
            typeString = parseAttribute("type", elements.get(0));
        } catch (CompilerException e) {}
        WikiMeta.Type type  = typeString != null
                ? WikiMeta.Type.valueOf(typeString.toUpperCase())
                : WikiMeta.Type.DOCUMENT;

        WikiMeta.Builder builder = new WikiMeta.Builder()
                .description(description)
                .status(WikiMeta.Status.valueOf(status))
                .type(type);

        if (type == WikiMeta.Type.SPELL) {
            Spell spell = parseSpellObject(elements.get(0));
            builder.object(spell);
        }

        return builder.build();
    }

    private Spell parseSpellObject(Element element) throws CompilerException{
        Spell.Builder builder = new Spell.Builder()
                .name(parseAttribute("name", element))
                .level(parseAttribute("level", element))
                .type(parseAttribute("casterType", element))
                .typeLevel(parseAttribute("casterTypeLevel", element))
                .castingTime(parseAttribute("castingTime", element))
                .manaConsumption(parseAttribute("manaConsumption", element))
                .range(parseAttribute("range", element))
                .description(parseAttribute("description", element))
                .effect(parseAttribute("effect", element));

        try {
            builder.duration(parseAttribute("duration", element));
        } catch (CompilerException e) {}
        try {
            builder.area(parseAttribute("area", element));
        } catch (CompilerException e) {
            builder.area("Single Target");
        }
        try {
            builder.savingThrow(parseAttribute("savingThrow", element));
        } catch (CompilerException e) {
            builder.savingThrow("None");
        }
        try {
            builder.notes(parseAttribute("notes", element));
        } catch (CompilerException e) {}
        try {
            builder.notes(parseAttribute("origin", element));
        } catch (CompilerException e) {
            builder.origin("Global");
        }
        return builder.build();
    }
}
