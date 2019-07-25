package com.pedrenrique.cryptonews.features.article

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.pedrenrique.cryptonews.R
import com.pedrenrique.cryptonews.config.MOCK_SERVER_DOMAIN
import com.pedrenrique.cryptonews.config.MOCK_SERVER_PORT
import com.pedrenrique.cryptonews.config.MOCK_SERVER_URL
import com.pedrenrique.cryptonews.core.data.Article
import com.pedrenrique.cryptonews.core.data.Source
import com.pedrenrique.cryptonews.features.news.NewsFragmentDirections
import com.pedrenrique.cryptonews.mock.MockDispatcher
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.StringContains.containsString
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import java.util.*

@RunWith(AndroidJUnit4::class)
@LargeTest
class ArticleFragmentTest {

    private val context = InstrumentationRegistry.getInstrumentation().context
    private val server = MockWebServer()

    @Before
    fun setUp() {
        server.apply {
            start(MOCK_SERVER_PORT)
            url(MOCK_SERVER_DOMAIN)
            setDispatcher(MockDispatcher(context))
        }
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun shouldShowArticle() {
        // Create a mock NavController
        val mockNavController = Mockito.mock(NavController::class.java)

        // Create a graphical FragmentScenario for the TitleScreen
        val publishedAt = Date(2019, 7, 25)
        val article = Article(
            "Title",
            "Description",
            "Content",
            MOCK_SERVER_URL + MockDispatcher.IMAGE_ENDPOINT,
            publishedAt,
            "Author",
            Source(null, "Source"),
            MOCK_SERVER_URL
        )
        val fragmentArgs = NewsFragmentDirections.showArticle(article).arguments
        val scenario = launchFragmentInContainer<ArticleFragment>(fragmentArgs)

        // Set the NavController property on the fragment
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }

        onView(withId(R.id.tvArticleTitle))
            .check(matches(withText(containsString(article.title))))

        onView(withId(R.id.tvArticleAuthor))
            .check(matches(withText(containsString(article.originalAuthor))))

        onView(withId(R.id.tvArticleContent))
            .check(matches(withText(containsString(article.content))))

        onView(withId(R.id.ivArticleImage))
            .check(matches(isDisplayed()))

        onView(withId(R.id.btnReadMore))
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowArticleAlternative() {
        // Create a mock NavController
        val mockNavController = Mockito.mock(NavController::class.java)

        // Create a graphical FragmentScenario for the TitleScreen
        val publishedAt = Date(2019, 7, 25)
        val article = Article(
            "Title",
            "Description",
            null,
            null,
            publishedAt,
            null,
            Source(null, "Source"),
            MOCK_SERVER_URL
        )
        val fragmentArgs = NewsFragmentDirections.showArticle(article).arguments
        val scenario = launchFragmentInContainer<ArticleFragment>(fragmentArgs)

        // Set the NavController property on the fragment
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }

        onView(withId(R.id.tvArticleTitle))
            .check(matches(withText(containsString(article.title))))

        onView(withId(R.id.tvArticleAuthor))
            .check(matches(withText(containsString(article.source.name))))

        onView(withId(R.id.tvArticleContent))
            .check(matches(withText(containsString(article.description))))

        onView(withId(R.id.ivArticleImage))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    fun canOpenArticleUrl() {
        // Create a mock NavController
        val mockNavController = Mockito.mock(NavController::class.java)

        // Create a graphical FragmentScenario for the TitleScreen
        val publishedAt = Date(2019, 7, 25)
        val article = Article(
            "Title",
            "Description",
            "Content",
            MOCK_SERVER_URL + MockDispatcher.IMAGE_ENDPOINT,
            publishedAt,
            "Author",
            Source(null, "Source"),
            MOCK_SERVER_URL
        )
        val fragmentArgs = NewsFragmentDirections.showArticle(article).arguments
        val scenario = launchFragmentInContainer<ArticleFragment>(fragmentArgs)

        // Set the NavController property on the fragment
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }

        onView(withId(R.id.btnReadMore))
            .perform(click())

        intended(allOf(hasAction(Intent.ACTION_VIEW), hasData(Uri.parse(article.url))))
    }
}