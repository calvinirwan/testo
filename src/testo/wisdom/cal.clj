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

(def skor (atom))
(defn put-answer
  "put salah benar ke db untuk masing2 user"
  [user-id qid answer]
  (* 1 2)
  )
