package com.gillessed.dnd.page.compiler;

import com.gillessed.dnd.page.compiler.exception.CompilerException;
import com.gillessed.dnd.page.parser.Element;
import com.gillessed.dnd.rest.model.page.WikiObject;

import java.util.List;

public interface ObjectCompiler {
    WikiObject compileObject(List<Element> elements) throws CompilerException;
}
