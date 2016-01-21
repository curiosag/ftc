package org.cg.ftc.shared.structures;

import java.util.HashMap;
import java.util.List;

import org.cg.common.check.Check;
import org.cg.common.structures.OrderedIntTuple;
import org.cg.common.util.CollectionUtil;
import org.cg.ftc.shared.interfaces.SqlCompletionType;

import com.google.common.base.Optional;

public class Completions {
	private HashMap<String, AbstractCompletion> nameToCompletion = new HashMap<String, AbstractCompletion>(); 
	
	public final Optional<OrderedIntTuple> replacementBoundaries;
	
	public Completions(Optional<OrderedIntTuple> replacementBoundaries)
	{
		this.replacementBoundaries = replacementBoundaries;
	}
	
	public static Completions create(Optional<OrderedIntTuple> replacementBoundaries, SqlCompletionType completionType, String name, String snippet)
	{
		Completions result = new Completions(replacementBoundaries);
		result.addSnippet(completionType, name, snippet);
		return result;
	}
	
	public CodeSnippetCompletion addSnippet(SqlCompletionType completionType, String name, String snippet){
		CodeSnippetCompletion  result = new CodeSnippetCompletion(completionType, name, snippet);
		nameToCompletion.put(name, result);
		return result;
	};
	
	public ModelElementCompletion addModelElement(SqlCompletionType type, String displayName, AbstractCompletion parent){
		ModelElementCompletion result = new ModelElementCompletion(type, displayName,  parent);
		nameToCompletion.put(displayName, result);
		return result;
	};
	
	public void addAll(Completions completions)
	{
		for (AbstractCompletion c : completions.getAll()) 
			nameToCompletion.put(c.displayName, c);
	}
	
	public AbstractCompletion getCompletion(String name){
		AbstractCompletion c = nameToCompletion.get(name);
		Check.notNull(c);
		return c;
	}
	
	public List<AbstractCompletion> getAll()
	{
		return CollectionUtil.toList(nameToCompletion.values());
	}
	
	public int size()
	{
		return nameToCompletion.size();
	}

	public void add(AbstractCompletion c) {
		nameToCompletion.put(c.displayName, c);
	}

}
