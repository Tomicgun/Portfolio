package main

import (
	"context"
	"encoding/json"
	"fmt"
	"github.com/aws/aws-sdk-go-v2/aws"
	"github.com/aws/aws-sdk-go-v2/config"
	"github.com/aws/aws-sdk-go-v2/service/dynamodb"
	"github.com/aws/aws-sdk-go-v2/service/dynamodb/types"
	"github.com/google/uuid"
	"github.com/jamespearly/loggly"
	"io"
	"net/http"
	"strings"
	"time"
)

type DailyPuzzle struct {
	Title string `json:"title"`
	URL   string `json:"url"`
	Image string `json:"image"`
	UUID  string
}

func main() {
	cfg, err := config.LoadDefaultConfig(context.TODO(), func(o *config.LoadOptions) error {
		o.Region = "us-east-1"
		return nil
	})
	if err != nil {
		panic(err)
	}
	Svc := dynamodb.NewFromConfig(cfg)

	var tag string
	tag = "Error"

	client := loggly.New(tag)
	//var interval = 600 //has to be 20 seconds minimum
	var httpLink = "https://api.chess.com/pub/puzzle/random"

	//ticker := time.NewTicker(time.Duration(*&interval) * time.Second)
	//done := make(chan bool)
	var Res DailyPuzzle

	for {
		buf := new(strings.Builder)
		resp, err := http.Get(*&httpLink)
		if err != nil {
			err := client.EchoSend("info", err.Error())
			if err != nil {
				fmt.Println(err.Error())
			}
		}

		//fmt.Println("Response status:", resp.Status)

		n, err := io.Copy(buf, resp.Body)
		if err != nil || n == 0 {
			err := client.EchoSend("info", err.Error())
			if err != nil {
				fmt.Println(err.Error())
			}
		}

		err2 := json.Unmarshal([]byte(buf.String()), &Res)
		Res.UUID = uuid.NewString()

		if err2 != nil {
			err := client.EchoSend("info", err.Error())
			if err != nil {
				fmt.Println(err.Error())
			}
		}
		if err != nil {
			err := client.EchoSend("info", err.Error())
			if err != nil {
				fmt.Println(err.Error())
			}
		}

		_, err = Svc.PutItem(context.TODO(), &dynamodb.PutItemInput{
			TableName: aws.String("tmarten_table"),
			Item: map[string]types.AttributeValue{
				"Title": &types.AttributeValueMemberS{Value: Res.Title},
				"URL":   &types.AttributeValueMemberS{Value: Res.URL},
				"Image": &types.AttributeValueMemberS{Value: Res.Image},
				"UUID":  &types.AttributeValueMemberS{Value: Res.UUID},
			},
		})
		time.Sleep(10 * time.Minute)
	}
}
