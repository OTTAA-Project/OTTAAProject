package com.stonefacesoft.ottaa.test;

import com.stonefacesoft.ottaa.test.unitTesting.UnitTestingConnectionDetector;
import com.stonefacesoft.ottaa.test.unitTesting.UnitTestingCreatePictograms;
import com.stonefacesoft.ottaa.test.unitTesting.UnitTestingFavoritePhrases;
import com.stonefacesoft.ottaa.test.unitTesting.UnitTestingGame;
import com.stonefacesoft.ottaa.test.unitTesting.UnitTestingGetPictogramsById;
import com.stonefacesoft.ottaa.test.unitTesting.UnitTestingGoogleTranslate;
import com.stonefacesoft.ottaa.test.unitTesting.UnitTestingGroups;
import com.stonefacesoft.ottaa.test.unitTesting.UnitTestingJson;
import com.stonefacesoft.ottaa.test.unitTesting.UnitTestingPhrases;
import com.stonefacesoft.ottaa.test.unitTesting.UnitTestingPremiumUser;
import com.stonefacesoft.ottaa.test.unitTesting.UnitTestingScore;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({UnitTestingConnectionDetector.class, UnitTestingCreatePictograms.class, UnitTestingFavoritePhrases.class, UnitTestingGame.class, UnitTestingGetPictogramsById.class, UnitTestingGroups.class, UnitTestingJson.class, UnitTestingPhrases.class, UnitTestingPremiumUser.class, UnitTestingScore.class, UnitTestingGoogleTranslate.class})
public class JUnitSuiteClasses {
 public static int testRunning;
}
