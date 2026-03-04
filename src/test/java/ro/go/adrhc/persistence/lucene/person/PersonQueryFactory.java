package ro.go.adrhc.persistence.lucene.person;

import lombok.experimental.UtilityClass;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.bare.query.FieldQueries;

import java.util.Set;

import static ro.go.adrhc.persistence.lucene.core.bare.query.BooleanQueryFactory.mustSatisfy;

@UtilityClass
public class PersonQueryFactory {
	public static final FieldQueries NAME_WORD_QUERIES = FieldQueries.create(PersonSchema.nameWord);
	public static final FieldQueries NAME_QUERIES = FieldQueries.create(PersonSchema.name);
	public static final FieldQueries ALIAS_KEYWORD_QUERIES =
		FieldQueries.create(PersonSchema.aliasKeyWord);
	public static final FieldQueries ALIAS_WORD_QUERIES = FieldQueries.create(PersonSchema.aliasWord);
	public static final FieldQueries ALIAS_PHRASE_QUERIES =
		FieldQueries.create(PersonSchema.aliasPhrase);
	public static final FieldQueries CNP_QUERIES = FieldQueries.create(PersonSchema.cnp);
	public static final FieldQueries ID_QUERIES = FieldQueries.create(PersonSchema.id);
	public static final FieldQueries MALE_QUERIES = FieldQueries.create(PersonSchema.male);
	public static final FieldQueries TAGS_QUERY = FieldQueries.create(PersonSchema.tags);

	public static Query hasTag(String tag) {
		return TAGS_QUERY.tokenEquals(tag);
	}

	public static Query hasAllTags(Set<String> tags) {
		return mustSatisfy(tags.stream().map(PersonQueryFactory::hasTag).toList());
	}
}
