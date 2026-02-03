package ro.go.adrhc.persistence.lucene.person;

import lombok.experimental.UtilityClass;
import org.apache.lucene.search.Query;
import ro.go.adrhc.persistence.lucene.core.bare.query.FieldQueries;

import java.util.Locale;
import java.util.Set;

import static ro.go.adrhc.persistence.lucene.core.bare.query.BooleanQueryFactory.mustSatisfy;

@UtilityClass
public class PersonQueryFactory {
	public static final FieldQueries NAME_WORD_QUERIES = FieldQueries.create(PersonFieldType.nameWord);
	public static final FieldQueries NAME_QUERIES = FieldQueries.create(PersonFieldType.name);
	public static final FieldQueries ALIAS_KEYWORD_QUERIES =
		FieldQueries.create(PersonFieldType.aliasKeyWord);
	public static final FieldQueries ALIAS_WORD_QUERIES = FieldQueries.create(PersonFieldType.aliasWord);
	public static final FieldQueries ALIAS_PHRASE_QUERIES =
		FieldQueries.create(PersonFieldType.aliasPhrase);
	public static final FieldQueries CNP_QUERIES = FieldQueries.create(PersonFieldType.cnp);
	public static final FieldQueries ID_QUERIES = FieldQueries.create(PersonFieldType.id);
	public static final FieldQueries MALE_QUERIES = FieldQueries.create(PersonFieldType.male);
	public static final FieldQueries TAGS_QUERY = FieldQueries.create(PersonFieldType.tags);

	public static Query hasTag(String tag) {
		return TAGS_QUERY.tokenEquals(tag.toLowerCase(Locale.ROOT));
	}

	public static Query hasAllTags(Set<String> tags) {
		return mustSatisfy(tags.stream().map(PersonQueryFactory::hasTag).toList());
	}
}
