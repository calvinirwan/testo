(ns testo.wisdom.cal
  (:import java.util.Date))

(def cal "vin")
(def twits (atom []))

(def twit-keys [:name :message :timestamp])
(defn clean-twit [twit]
  (-> twit
      (select-keys twit-keys)
      (assoc :timestamp (System/currentTimeMillis))))

(def MAX-TWITS 5)
(defn get-twits [] @twits)
(defn get-twit [ii] (nth @twits ii))

(defn put-twit! [twit]
  (swap! twits #(take MAX-TWITS (conj % (clean-twit twit)))))

(defn GET-index []
  {:status 200
   :body (get-twits)
   :headers {}})

(defn POST-index [name message]
  (put-twit! {:name name :message message})
  (GET-index))

(def question (atom [{:qid 1
                      :qtext "apalah ?"
                      :qanswer {:selection [1 2 3]
                                :answer 3}}
                     {:qid 2
                      :qtext "apalah 2 ?"
                      :qanswer {:selection [1 2 3]
                                :answer 1}}
                     {:qid 3
                      :qtext "apalah 3 ?"
                      :qanswer {:selection [1 2 3]
                                :answer 2}}]))

(defn get-question [qid] (nth @question qid) )

(defn benarkah? [qid answer]
  (let [question (get-question qid)
        correct-answer (:answer (:qanswer question))]
    (if (= answer correct-answer)
      "benar"
      "salah")))

(def answer-u (atom [{:user-id 1 :qid 1 :answer 1}
                     {:user-id 2 :qid 1 :answer 3}
                     {:user-id 1 :qid 2 :answer 2}]))


(def user0 {:name "budi"})
(def user1 {:name "calvin"})

(def answer0 {:correct-answer "lala"})
(def answer10 {:correct-answer "brute"})

(def question0 {:text "what ?"
                :answer answer0})
(def question10 {:text "why ?"
                 :answer answer0})


(def user-answers (atom[{:user user1 :q question0 :u-ans "lala"}
                        {:user user0 :q question0 :u-ans "ba"
                         }
                        {:user user0 :q question10 :u-ans "ba"}]))

(def quiz0 {:quetions [question0
                       question10]})

(def skor (atom []))
(defn put-answer
  "put salah benar ke db untuk masing2 user"
  [user q u-ans]
  (swap! user-answers (conj @user-answers {:user user :q q :u-ans u-ans}))
  )


(defn validate-answer
  [q u-ans]
  (= (:correct-answer (:answer q)) (:u-ans u-ans)))

(defn get-result-qusetion
  [user-answer]
  (validate-answer (:q user-answer) (:u-ans user-answer)))

(defn user-answer?
  [u-question u-ans]
  (let [u-ans (map second u-ans)]
    (every? identity (map (fn [a] (some #(= a %) u-ans))  u-question))
      ))

[{:a 'a0
 :b 'q0
  :c 'c0}
 {:a 'a0
 :b 'q1
  :c 'c0}
 {:a 'a1
 :b 'q0
  :c 'c0}]

[{:a 'a0
 :b 'q0
  :c 'c0}
 {:a 'a0
 :b 'q1
  :c 'c0}]

(defn get-user-answer-from-questions
  [user questions u-ans]
  (let [u-question (map #(conj [user] %) questions)]
    (map (fn [a] (filter #(user-answer? a %) u-ans))  u-question)))

(defn get-user-answer
  [user quiz u-ans]
  (let [questions (:questions quiz)
        u-ans (get-user-answer-from-questions user questions u-ans)]
    u-ans))

(defn get-score
  [])


(defn get-result-quiz
  [user quiz]
  (let [user-ans (get-user-answer user quiz)
        score (get-score user-ans)
        history (get-history user-ans)]
    {:score score :history history}))
