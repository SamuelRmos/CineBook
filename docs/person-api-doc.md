Person

---
updatedAt: 2025-10-01T20:30:09.000Z
agentTools:
  projectIndex: https://developer.themoviedb.org/llms.txt
---

# Person

Search for people by their name and also known as names.

# OpenAPI definition

```json
{
  "openapi": "3.1.0",
  "info": {
    "title": "tmdb-api",
    "version": "3"
  },
  "servers": [
    {
      "url": "https://api.themoviedb.org"
    }
  ],
  "components": {
    "securitySchemes": {
      "sec0": {
        "type": "apiKey",
        "in": "header",
        "name": "Authorization",
        "x-bearer-format": "bearer"
      }
    }
  },
  "security": [
    {
      "sec0": []
    }
  ],
  "paths": {
    "/3/search/person": {
      "get": {
        "summary": "Person",
        "description": "Search for people by their name and also known as names.",
        "operationId": "search-person",
        "parameters": [
          {
            "name": "query",
            "in": "query",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "include_adult",
            "in": "query",
            "schema": {
              "type": "boolean",
              "default": false
            }
          },
          {
            "name": "language",
            "in": "query",
            "schema": {
              "type": "string",
              "default": "en-US"
            }
          },
          {
            "name": "page",
            "in": "query",
            "schema": {
              "type": "integer",
              "format": "int32",
              "default": 1
            }
          }
        ],
        "responses": {
          "200": {
            "description": "200",
            "content": {
              "application/json": {
                "examples": {
                  "Result": {
                    "value": "{\"page\":1,\"results\":[{\"adult\":false,\"gender\":2,\"id\":31,\"known_for_department\":\"Acting\",\"name\":\"Tom Hanks\",\"original_name\":\"Tom Hanks\",\"popularity\":84.631,\"profile_path\":\"/xndWFsBlClOJFRdhSt4NBwiPq2o.jpg\",\"known_for\":[{\"adult\":false,\"backdrop_path\":\"/3h1JZGDhZ8nzxdgvkxha0qBqi05.jpg\",\"id\":13,\"title\":\"Forrest Gump\",\"original_language\":\"en\",\"original_title\":\"Forrest Gump\",\"overview\":\"A man with a low IQ has accomplished great things in his life and been present during significant historic events—in each case, far exceeding what anyone imagined he could do. But despite all he has achieved, his one true love eludes him.\",\"poster_path\":\"/arw2vcBveWOVZr6pxd9XTd1TdQa.jpg\",\"media_type\":\"movie\",\"genre_ids\":[35,18,10749],\"popularity\":67.209,\"release_date\":\"1994-06-23\",\"video\":false,\"vote_average\":8.481,\"vote_count\":24525},{\"adult\":false,\"backdrop_path\":\"/3Rfvhy1Nl6sSGJwyjb0QiZzZYlB.jpg\",\"id\":862,\"title\":\"Toy Story\",\"original_language\":\"en\",\"original_title\":\"Toy Story\",\"overview\":\"Led by Woody, Andy's toys live happily in his room until Andy's birthday brings Buzz Lightyear onto the scene. Afraid of losing his place in Andy's heart, Woody plots against Buzz. But when circumstances separate Buzz and Woody from their owner, the duo eventually learns to put aside their differences.\",\"poster_path\":\"/uXDfjJbdP4ijW5hWSBrPrlKpxab.jpg\",\"media_type\":\"movie\",\"genre_ids\":[16,12,10751,35],\"popularity\":119.802,\"release_date\":\"1995-10-30\",\"video\":false,\"vote_average\":7.969,\"vote_count\":16613},{\"adult\":false,\"backdrop_path\":\"/vxJ08SvwomfKbpboCWynC3uqUg4.jpg\",\"id\":497,\"title\":\"The Green Mile\",\"original_language\":\"en\",\"original_title\":\"The Green Mile\",\"overview\":\"A supernatural tale set on death row in a Southern prison, where gentle giant John Coffey possesses the mysterious power to heal people's ailments. When the cell block's head guard, Paul Edgecomb, recognizes Coffey's miraculous gift, he tries desperately to help stave off the condemned man's execution.\",\"poster_path\":\"/o0lO84GI7qrG6XFvtsPOSV7CTNa.jpg\",\"media_type\":\"movie\",\"genre_ids\":[14,18,80],\"popularity\":77.553,\"release_date\":\"1999-12-10\",\"video\":false,\"vote_average\":8.507,\"vote_count\":15310}]}],\"total_pages\":1,\"total_results\":1}"
                  }
                },
                "schema": {
                  "type": "object",
                  "properties": {
                    "page": {
                      "type": "integer",
                      "example": 1,
                      "default": 0
                    },
                    "results": {
                      "type": "array",
                      "items": {
                        "type": "object",
                        "properties": {
                          "adult": {
                            "type": "boolean",
                            "example": false,
                            "default": true
                          },
                          "gender": {
                            "type": "integer",
                            "example": 2,
                            "default": 0
                          },
                          "id": {
                            "type": "integer",
                            "example": 31,
                            "default": 0
                          },
                          "known_for_department": {
                            "type": "string",
                            "example": "Acting"
                          },
                          "name": {
                            "type": "string",
                            "example": "Tom Hanks"
                          },
                          "original_name": {
                            "type": "string",
                            "example": "Tom Hanks"
                          },
                          "popularity": {
                            "type": "number",
                            "example": 84.631,
                            "default": 0
                          },
                          "profile_path": {
                            "type": "string",
                            "example": "/xndWFsBlClOJFRdhSt4NBwiPq2o.jpg"
                          },
                          "known_for": {
                            "type": "array",
                            "items": {
                              "type": "object",
                              "properties": {
                                "adult": {
                                  "type": "boolean",
                                  "example": false,
                                  "default": true
                                },
                                "backdrop_path": {
                                  "type": "string",
                                  "example": "/3h1JZGDhZ8nzxdgvkxha0qBqi05.jpg"
                                },
                                "id": {
                                  "type": "integer",
                                  "example": 13,
                                  "default": 0
                                },
                                "title": {
                                  "type": "string",
                                  "example": "Forrest Gump"
                                },
                                "original_language": {
                                  "type": "string",
                                  "example": "en"
                                },
                                "original_title": {
                                  "type": "string",
                                  "example": "Forrest Gump"
                                },
                                "overview": {
                                  "type": "string",
                                  "example": "A man with a low IQ has accomplished great things in his life and been present during significant historic events—in each case, far exceeding what anyone imagined he could do. But despite all he has achieved, his one true love eludes him."
                                },
                                "poster_path": {
                                  "type": "string",
                                  "example": "/arw2vcBveWOVZr6pxd9XTd1TdQa.jpg"
                                },
                                "media_type": {
                                  "type": "string",
                                  "example": "movie"
                                },
                                "genre_ids": {
                                  "type": "array",
                                  "items": {
                                    "type": "integer",
                                    "example": 35,
                                    "default": 0
                                  }
                                },
                                "popularity": {
                                  "type": "number",
                                  "example": 67.209,
                                  "default": 0
                                },
                                "release_date": {
                                  "type": "string",
                                  "example": "1994-06-23"
                                },
                                "video": {
                                  "type": "boolean",
                                  "example": false,
                                  "default": true
                                },
                                "vote_average": {
                                  "type": "number",
                                  "example": 8.481,
                                  "default": 0
                                },
                                "vote_count": {
                                  "type": "integer",
                                  "example": 24525,
                                  "default": 0
                                }
                              }
                            }
                          }
                        }
                      }
                    },
                    "total_pages": {
                      "type": "integer",
                      "example": 1,
                      "default": 0
                    },
                    "total_results": {
                      "type": "integer",
                      "example": 1,
                      "default": 0
                    }
                  }
                }
              }
            }
          }
        },
        "deprecated": false
      }
    }
  },
  "x-readme": {
    "headers": [],
    "explorer-enabled": true,
    "proxy-enabled": true
  },
  "x-readme-fauxas": true
}
```