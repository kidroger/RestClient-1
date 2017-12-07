package net.devfront.rest

import com.fasterxml.jackson.core.type.TypeReference
import org.apache.http.client.fluent.Request
import org.apache.http.entity.ContentType

/**
 * Test runner for RestClient using AWS PetStore API.
 *
 */
class TestRunner {
    private static final String BASE_URL = "http://petstore-demo-endpoint.execute-api.com/petstore"

    static void main(String[] args) {
        RestClient restClient = new RestClient()

        // retrieveForList
        String endpoint = String.format("%s/pets", BASE_URL)
        Request request = Request.Get(endpoint)

        List<Pet> petList = restClient.retrieveForList(request, new TypeReference<List<Pet>>() {})
        for (Pet pet : petList) {
            System.out.println(pet)
        }

        System.out.println("=============")

        // retrieveForObject
        endpoint = String.format("%s/pets/%d", BASE_URL, 1)
        request = Request.Get(endpoint)

        Pet pet = restClient.retrieveForObject(request, Pet.class)
        System.out.println(pet)
    }
}
