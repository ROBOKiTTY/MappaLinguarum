=Mappa Linguarum XML specification=

Data used by Mappa Linguarm is stored in Extensible Markup Language (XML) format. XML is a human-readable standard for programmatic data processing. The following specification defines how to create valid XML data for Mappa Linguarum.

For general XML syntax, see introduction at W3Schools: http://www.w3schools.com/xml/default.asp.

==Overall==

XML Version supported: 1.0

Encoding: As Mappa Linguarum needs to display non-ASCII Unicode characters, UTF-8 or above is recommended

==Language data==

Here's a minimal template, with explanations after:

<?xml version="1.0" encoding="UTF-8"?>
<languageMapData>
	<language>
		<name>English</name>
		<name>Anglais</name>
		<family>Indo-European</family>
		<family>Germanic</family>
		<feature>SVO</feature>
		<feature>Pluricentric</feature>
		<phoneme-consonants>p k t</phoneme-consonants>
		<phoneme-vowels>a i o</phoneme-vowels>
		<location>20,20,0 10,10,0 -10,-10,0 -11.424,-12.421,0</location>
		<information>English is a language spoken by people who speak English.</information>
		<link>http://www.example.com</link>
		<link>http://www.another-example.com</link>
		<dialect>
			<name>Canadian English</name>
			<name>Anglais canadien</name>
			<phoneme-consonants>p k t m</phoneme-consonants>
			<phoneme-vowels>a i o u</phoneme-vowels>
			<information>N/A.</information>
			<link>http://www.canada.com</link>
		</dialect>
	</language>
</languageMapData>

* A language can have multiple names, families, features, and links. However, the first name is taken to be the common name, and families are given in descending order.

* Features and families are user-defined. Make sure spelling and capitalization are consistent.

* IPA transcription symbols are supported for phonemes. Non-IPA symbols are unsupported and may return incorrect or unexpected results.

* Separate two phonemes with a single space.

* Location is composed of a series of vertices as in a polygon, each representing a longitude, latitude, and altitude. Altitude is unused and should be kept at 0. Separate the three with commas without preceding or trailing space. Separate each group with a single space. The first and last vertices need to be the same.

* A language can have many dialects, and an XML file can contain many languages.