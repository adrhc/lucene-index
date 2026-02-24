package ro.go.adrhc.persistence.lucene.core.bare.token;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.lang.String.join;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ro.go.adrhc.persistence.lucene.core.bare.token.AudioTokenizationUtils.AUDIO_TOKENIZER;

@Slf4j
class AudioTokenizationTest {
	@Test
	void audioTokenizerTest() throws IOException {
		assertTokens("Smiley - Vals (Official).mp3", "smiley vals");
		assertTokens("Smiley - Vals (Official Video) Version.mp3", "smiley vals version");
		assertTokens("Smiley - Vals (Official Video Version.mp3", "smiley vals");
		assertTokens("Smiley - Vals Official Video) Version.mp3", "smiley vals version");
		assertTokens("Smiley - Vals Official Video Version.mp3", "smiley vals");
		assertTokens("Yazoo - Don'$'\\'''t Go (Official HD Video) - Yaz.mp3", "yazoo don go yaz");
		assertTokens("test (Audio Version) title.mp3", "test title");
		assertTokens("The Wallflowers - One Headlight (Official Music Video).mp3",
			"the wallflowers one headlight");
		assertTokens("Dr Alban - Hello Africa (Official HD).mp3", "dr alban hello africa");
	}

	private static void assertTokens(String text, String expectedTokens) throws IOException {
		assertEquals(expectedTokens, join(" ", AUDIO_TOKENIZER.textToTokenList(text)));
	}
}
