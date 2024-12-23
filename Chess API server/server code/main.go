package main

//works
import (
	"context"
	"encoding/json"
	"fmt"
	"github.com/aws/aws-sdk-go-v2/aws"
	"github.com/aws/aws-sdk-go-v2/config"
	"github.com/aws/aws-sdk-go-v2/feature/dynamodb/attributevalue"
	"github.com/aws/aws-sdk-go-v2/feature/dynamodb/expression"
	"github.com/aws/aws-sdk-go-v2/service/dynamodb"
	"github.com/aws/aws-sdk-go-v2/service/dynamodb/types"
	"github.com/gorilla/mux"
	"github.com/jamespearly/loggly"
	"log"
	"net/http"
	"net/url"
	"os"
	"reflect"
	"regexp"
	"time"
	"unicode/utf8"
)

type StatusResponseWriter struct {
	http.ResponseWriter
	statusCode int
}

type statusResponse struct {
	Table  string `json:"table"`
	Items  string `json:"items"`
	Time   string `json:"time"`
	Status string `json:"status"`
}

type DailyPuzzle struct {
	Title string `json:"title"`
	URL   string `json:"url"`
	Image string `json:"image"`
	UUID  string
}

func main() {

	r := mux.NewRouter()
	r.HandleFunc("/tmarten/status", StatusHandler).Methods("GET")
	r.HandleFunc("/tmarten/all", AllHandler).Methods("GET")
	r.HandleFunc("/tmarten/search", SearchHandler).Methods("GET")
	r.Methods("POST", "DELETE", "PUT", "PATCH", "HEAD", "OPTIONS").HandlerFunc(BadMethodHandler)
	r.PathPrefix("/").HandlerFunc(CatchAllHandler)
	r.Use(RequestLogglyMiddleware(r))
	err := http.ListenAndServe(":8080", r)
	if err != nil {
		println(err.Error())
	}
}

func SearchHandler(w http.ResponseWriter, r *http.Request) {
	var err error
	var response *dynamodb.QueryOutput

	u, err := url.Parse(r.URL.String())
	if err != nil {
		fmt.Println(err.Error())
	}
	var UUID = ""
	if u != nil {
		UUID = u.Query().Get("UUID")
	}
	if UUID == "" {
		rep := &statusResponse{
			Table:  "tmarten_table",
			Items:  fmt.Sprint("No UUID given please provide valid UUID"),
			Time:   time.Now().Format("2006-01-02 15:04:05"),
			Status: "400"}
		respj, _ := json.MarshalIndent(rep, "", "    ")
		_, _ = w.Write(respj)
		return
	}

	if reflect.TypeOf(UUID).Kind() != reflect.String {
		_, err := w.Write([]byte("Error 400 bad request Name not string\n"))
		if err != nil {
			return
		}
	}
	p1 := "^[A-Za-z0-9][A-Za-z0-9_-]*$"
	b, _ := regexp.MatchString(p1, UUID)
	if b == false {
		_, err := w.Write([]byte("Error 400 bad request not allowed characters\n"))
		if err != nil {
			return
		}
	}
	if utf8.RuneCountInString(UUID) > 36 {
		_, err := w.Write([]byte("Error 400 bad request name too long\n"))
		if err != nil {
			return
		}
	}
	cfg, err := config.LoadDefaultConfig(context.TODO(), func(o *config.LoadOptions) error {
		o.Region = "us-east-1"
		return nil
	})
	if err != nil {
		panic(err)
	}
	Svc := dynamodb.NewFromConfig(cfg)
	key := expression.Key("UUID").Equal(expression.Value(UUID))
	expr, err := expression.NewBuilder().WithKeyCondition(key).Build()
	response, err = Svc.Query(context.TODO(), &dynamodb.QueryInput{
		TableName:                 aws.String("tmarten_table"),
		ExpressionAttributeNames:  expr.Names(),
		ExpressionAttributeValues: expr.Values(),
		KeyConditionExpression:    expr.KeyCondition(),
	})
	if err != nil {
		fmt.Println(err)
	}
	if response.Count != 0 {
		responsejson, _ := json.MarshalIndent(response.Items, "", "    ")
		_, err = w.Write(responsejson)
		if err != nil {
			println(err.Error())
		}
		w.WriteHeader(200)
		return
	} else {
		rep := &statusResponse{
			Table:  "tmarten_table",
			Items:  fmt.Sprint("No items found"),
			Time:   time.Now().Format("2006-01-02 15:04:05"),
			Status: "400"}
		respj, _ := json.Marshal(rep)
		_, e := w.Write(respj)
		if e != nil {
			return
		}
	}
}

func AllHandler(w http.ResponseWriter, r *http.Request) {
	cfg, er := config.LoadDefaultConfig(context.TODO(), func(o *config.LoadOptions) error {
		o.Region = "us-east-1"
		return nil
	})
	if er != nil {
		panic(er)
	}
	Svc := dynamodb.NewFromConfig(cfg)
	out, err := Svc.Scan(context.TODO(), &dynamodb.ScanInput{
		TableName: aws.String("tmarten_table"),
	})
	if err != nil {
		panic(err)
	}
	responsejson, _ := json.MarshalIndent(out.Items, "", "    ")
	_, errr := w.Write(responsejson)
	if errr != nil {
		return
	}
	fmt.Println(out.Count)
	return
}

func StatusHandler(w http.ResponseWriter, r *http.Request) {
	cfg, er := config.LoadDefaultConfig(context.TODO(), func(o *config.LoadOptions) error {
		o.Region = "us-east-1"
		return nil
	})
	if er != nil {
		panic(er)
	}
	Svc := dynamodb.NewFromConfig(cfg)
	out, err := Svc.Scan(context.TODO(), &dynamodb.ScanInput{
		TableName: aws.String("tmarten_table"),
	})
	if err != nil {
		panic(err)
	}
	response := &statusResponse{
		Table:  "tmarten_table",
		Items:  fmt.Sprint(out.Count),
		Time:   time.Now().Format("2006-01-02 15:04:05"),
		Status: "200"}
	responsejson, _ := json.MarshalIndent(response, "", "    ")
	_, e := w.Write(responsejson)
	if e != nil {
		return
	}
	w.WriteHeader(200)
	return
}

func CatchAllHandler(w http.ResponseWriter, r *http.Request) {
	_, err := fmt.Fprintf(w, "Error 404 This access has been logged")
	if err != nil {
		return
	}
	w.WriteHeader(404)
}

func BadMethodHandler(w http.ResponseWriter, r *http.Request) {
	_, err := fmt.Fprintf(w, "Error 405 This access has been logged")
	if err != nil {
		return
	}
	w.WriteHeader(405)
}

func NewStatusResponseWriter(w http.ResponseWriter) *StatusResponseWriter {
	return &StatusResponseWriter{
		ResponseWriter: w,
	}
}

func (sw *StatusResponseWriter) WriteHeader(statusCode int) {
	sw.statusCode = statusCode
	sw.ResponseWriter.WriteHeader(statusCode)
}

// Source https://stackoverflow.com/questions/27234861/correct-way-of-getting-clients-ip-addresses-from-http-request
// Author mel3kings Link:https://stackoverflow.com/users/2023728/mel3kings
func ReadUserIP(r *http.Request) string {
	IPAddress := r.Header.Get("X-Real-Ip")
	if IPAddress == "" {
		IPAddress = r.Header.Get("X-Forwarded-For")
	}
	if IPAddress == "" {
		IPAddress = r.RemoteAddr
	}
	return IPAddress
}

// source https://www.thecodersstop.com/golang/simple-http-request-logging-middleware-in-go/
func RequestLoggerMiddleware(r *mux.Router) mux.MiddlewareFunc {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, req *http.Request) {

			start := time.Now()
			sw := NewStatusResponseWriter(w)
			ip := ReadUserIP(req)

			defer func() {
				log.Printf(
					"[%s] [%v] [%d] %s %s %s %s",
					req.Method,
					time.Since(start),
					sw.statusCode,
					req.Host,
					req.URL.Path,
					req.URL.RawQuery,
					ip,
				)
			}()
			next.ServeHTTP(w, req)
		})
	}
}

func RequestLogglyMiddleware(r *mux.Router) mux.MiddlewareFunc {
	return func(next http.Handler) http.Handler {
		return http.HandlerFunc(func(w http.ResponseWriter, req *http.Request) {
			start := time.Now()
			sw := NewStatusResponseWriter(w)
			ip := ReadUserIP(req)
			var tag string
			tag = "error"
			logy := loggly.New(tag)
			defer func() {
				err := logy.EchoSend("error", fmt.Sprintf(
					"[%s] [%v] [%d] %s %s %s",
					req.Method,
					time.Since(start),
					sw.statusCode,
					req.Host,
					req.URL.Path,
					ip))
				if err != nil {
					fmt.Println(os.Stderr, err.Error())
				}
			}()
			next.ServeHTTP(w, req)
		})
	}

}

func (puzzle DailyPuzzle) GetKey() map[string]types.AttributeValue {
	title, err := attributevalue.Marshal(puzzle.Title)
	if err != nil {
		panic(err)
	}
	return map[string]types.AttributeValue{"title": title}
}
