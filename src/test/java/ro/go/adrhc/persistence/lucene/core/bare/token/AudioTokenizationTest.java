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
		assertAudioTokens("Smiley - Vals (Official).mp3", "smiley vals");
		assertAudioTokens("Smiley - Vals (Official Video) Version.mp3", "smiley vals version");
		assertAudioTokens("Smiley - Vals (Official Video Version.mp3", "smiley vals");
		assertAudioTokens("Smiley - Vals Official Video) Version.mp3", "smiley vals version");
		assertAudioTokens("Smiley - Vals Official Video Version.mp3", "smiley vals");
		assertAudioTokens("Yazoo - Don'$'\\'''t Go (Official HD Video) - Yaz.mp3", "yazoo don go yaz");
		assertAudioTokens("test (Audio Version) title.mp3", "test title");
		assertAudioTokens("The Wallflowers - One Headlight (Official Music Video).mp3",
			"the wallflowers one headlight");
		assertAudioTokens("Dr Alban - Hello Africa (Official HD).mp3", "dr alban hello africa");
	}

	public static void assertAudioTokens(String text, String expectedTokens) throws IOException {
		assertEquals(expectedTokens, join(" ", AUDIO_TOKENIZER.textToTokenList(text)));
	}
}
