/*
 * Copyright 2020-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.fn.supplier.mail;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import org.springframework.messaging.Message;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(properties = {
	"mail.supplier.idle-imap=true",
	"mail.supplier.url=imap://user:pw@localhost:${test.mail.server.imap.port}/INBOX"})
public class ImapIdleFailTests extends AbstractMailSupplierTests {
	@Test
	public void testSimpleTest() {
		// given
		sendMessage("test", "foo");
		// when
		final Flux<Message<?>> messageFlux = mailSupplier.get();
		// then
		StepVerifier.create(messageFlux)
			.assertNext(message -> {
					assertThat(((String) message.getPayload())).isNotEqualTo("Test Mail");
				}
			)
			.thenCancel()
			.verify();
	}
}
