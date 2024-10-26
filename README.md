Ahhoz hogy elinfistsa otthon a projektett Anroid Stoudio / IntelJ-ben fel kell instalálnija a KotlinMultiplatform és Kotlin pluginokat
illetve legyen a mind a kotlin a legfrisseb. 
Miután betöltöte a projektet fel kel hogy jöjön egy warning arról hogy az a lokális adatbázist nem találja a projekt ezzel nincsen semmi baj automatikusan beiltja a helyi gépen.
Lehet hogy feljön egy javaslat hogy: Firosteni akkarja-e a projekt gradel verzióját? NE FRISSITSE A GRADEL VERZIÓT! A kotlinmultiplatfromprojekt plugin nem kompitibilis a legújabb verzióval úgy nem fog müködni.
Futatni terminálból a "./gradlew run" vagy "./gradlew composeApp:run"
De én áltálában futatás zöldháromszögétöl balra iconra katintva feljön az "Edit Configuration..." feljön egy dialog ott "+"-re katint majd kiválasza a Gradel.t végül a RUN sáv ba beileszti hogy "composeApp:run" OK és már képes futani a zöld háromszöggel.
